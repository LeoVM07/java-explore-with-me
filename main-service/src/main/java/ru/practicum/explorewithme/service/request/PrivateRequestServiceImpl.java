package ru.practicum.explorewithme.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dal.EventRepository;
import ru.practicum.explorewithme.dal.RequestRepository;
import ru.practicum.explorewithme.dal.UserRepository;
import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.dto.request.RequestStatusUpdateDto;
import ru.practicum.explorewithme.dto.request.RequestUpdateResponseDto;
import ru.practicum.explorewithme.exception.*;
import ru.practicum.explorewithme.mapper.RequestMapper;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.Request;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.utility.enums.EventState;
import ru.practicum.explorewithme.utility.enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getUserRequests(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new UserIdException(userId);
        }
        return requestRepository.findByRequesterId(userId).stream().map(requestMapper::toDto).toList();
    }

    @Override
    public RequestDto addRequest(Long userId, Long eventId) {
        User user = checkUser(userId);
        Event event = checkEvent(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new InvalidRequestDataException("Пользователь не может записаться на событие, которое организовывает");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new InvalidRequestDataException("Записаться можно только на опубликованные события");
        }

        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new InvalidRequestDataException("Запрос на событие уже отправлен");
        }

        if (event.getParticipantLimit() > 0 &&
                event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new InvalidRequestDataException("На событие уже набрался максимум участников");
        }

        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);
        request.setStatus(event.getRequestModeration() && event.getParticipantLimit() > 0 ?
                RequestStatus.PENDING : RequestStatus.CONFIRMED);

        if (request.getStatus() == RequestStatus.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        Request savedRequest = requestRepository.save(request);
        log.info("Сохранение запроса на событие: {}", savedRequest);
        return requestMapper.toDto(savedRequest);
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new RequestIdException(requestId));

        if (!request.getRequester().getId().equals(userId)) {
            throw new InvalidRequestDataException("Отменить можно только свой запрос на участие в событии");
        }

        if (request.getStatus() == RequestStatus.CONFIRMED) {
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }

        request.setStatus(RequestStatus.CANCELED);
        Request savedRequest = requestRepository.save(request);
        return requestMapper.toDto(savedRequest);
    }


    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getUserEventRequests(Long userId, Long eventId) {

        if (!userRepository.existsById(userId)) {
            throw new UserIdException(userId);
        }
        if (!eventRepository.existsById(eventId)) {
            throw new EventIdException(eventId);
        }

        return requestRepository.findByEventId(eventId).stream().map(requestMapper::toDto).toList();
    }


    @Override
    public RequestUpdateResponseDto updateRequestStatus(Long userId, Long eventId,
                                                        RequestStatusUpdateDto statusUpdateDto) {
        if (!userRepository.existsById(userId)) {
            throw new UserIdException(userId);
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventIdException(eventId));

        List<Request> requests = requestRepository.findByIdIn(statusUpdateDto.getRequestIds());

        if (requests.size() != statusUpdateDto.getRequestIds().size()) {
            throw new NotFoundException("Некоторые запросы не были найдены");
        }

        requests.forEach(request -> {
            if (!request.getEvent().getId().equals(eventId)) {
                throw new InvalidRequestDataException("Запрос относится к другому событию");
            }
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new InvalidRequestDataException("Статус запроса уже был установлен");
            }
        });

        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();

        if (statusUpdateDto.getStatus() == RequestStatus.CONFIRMED) {

            long availableSlots = event.getParticipantLimit() > 0 ?
                    event.getParticipantLimit() - event.getConfirmedRequests() : Integer.MAX_VALUE;

            if (event.getParticipantLimit() > 0 && availableSlots <= 0) {
                throw new InvalidRequestDataException("Событие уже достигло максимума участников");
            }

            if (event.getParticipantLimit() > 0 && requests.size() > availableSlots) {
                throw new InvalidRequestDataException(String.format(
                        "Невозможно подтвердить %d запросов. Свободных мест на событии осталось: %d",
                        requests.size(), availableSlots));
            }

            requests.forEach(request -> {
                request.setStatus(RequestStatus.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                confirmedRequests.add(requestMapper.toDto(request));
            });

            if (event.getParticipantLimit() > 0 &&
                    event.getConfirmedRequests() >= event.getParticipantLimit()) {

                List<Request> otherPendingRequests = requestRepository
                        .findByEventIdAndStatus(eventId, RequestStatus.PENDING);

                otherPendingRequests.forEach(request -> {
                    request.setStatus(RequestStatus.REJECTED);
                    rejectedRequests.add(requestMapper.toDto(request));
                });

                if (!otherPendingRequests.isEmpty()) {
                    requestRepository.saveAll(otherPendingRequests);
                }
            }

        } else if (statusUpdateDto.getStatus() == RequestStatus.REJECTED) {
            requests.forEach(request -> {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(requestMapper.toDto(request));
            });
        }

        requestRepository.saveAll(requests);
        eventRepository.save(event);

        return new RequestUpdateResponseDto(confirmedRequests, rejectedRequests);
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EventIdException(eventId));
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserIdException(userId));
    }
}
