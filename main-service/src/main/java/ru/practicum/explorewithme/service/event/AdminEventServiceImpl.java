package ru.practicum.explorewithme.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dal.CategoryRepository;
import ru.practicum.explorewithme.dal.EventRepository;
import ru.practicum.explorewithme.dto.event.EventDto;
import ru.practicum.explorewithme.dto.event.UpdateAdminEventDto;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.CategoryIdException;
import ru.practicum.explorewithme.exception.InvalidRequestDataException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.mapper.LocationMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.Location;
import ru.practicum.explorewithme.utility.enums.EventState;
import ru.practicum.explorewithme.utility.enums.StateAction;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;

    @Autowired
    public AdminEventServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository,
                                 EventMapper eventMapper, LocationMapper locationMapper) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.eventMapper = eventMapper;
        this.locationMapper = locationMapper;

    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<Event> events = eventRepository.findAll(pageable).getContent();

        events = events.stream()
                .filter(event -> users == null || users.isEmpty() || users.contains(event.getInitiator().getId()))
                .filter(event -> states == null || states.isEmpty() || states.contains(event.getState()))
                .filter(event -> categories == null || categories.isEmpty() || categories.contains(event.getCategory().getId()))
                .filter(event -> rangeStart == null || !event.getEventDate().isBefore(rangeStart))
                .filter(event -> rangeEnd == null || !event.getEventDate().isAfter(rangeEnd))
                .toList();

        return events.stream()
                .map(eventMapper::toEventDto)
                .toList();
    }

    @Override
    public EventDto updateEvent(Long eventId, UpdateAdminEventDto updateRequest) {
        log.debug("Получен запрос на обновление события");
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id= %d не найдено", eventId)));

        if (updateRequest.getEventDate() != null && updateRequest.getEventDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Изменяемая дата не может быть в прошлом");
        }

        validateEventState(event, updateRequest.getStateAction());
        changeEventState(event, updateRequest.getStateAction());
        updateEventFields(event, updateRequest);

        log.debug("Сборка события для ответа");

        return eventMapper.toEventDto(event);
    }

    private void validateEventState(Event event, StateAction state) {
        if (state == null) return;

        if (state == StateAction.PUBLISH_EVENT && event.getState() != EventState.PENDING) {
            throw new InvalidRequestDataException("Только события в статусе ожидания могут быть опубликованы");
        }
        if (state == StateAction.REJECT_EVENT && event.getState() == EventState.PUBLISHED) {
            throw new InvalidRequestDataException("Только неопубликованные события могут быть отменены");
        }
    }

    private void changeEventState(Event event, StateAction state) {
        if (state == null) return;

        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestDataException("Дата события не может быть в прошлом");
        }

        if (state == StateAction.PUBLISH_EVENT) {
            if ((event.getEventDate().isBefore(LocalDateTime.now().plusHours(1)))) {
                throw new InvalidRequestDataException("Событие должно начинаться не ранее, " +
                        "чем через час от текущего времени");
            }
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        }

        if (state == StateAction.REJECT_EVENT) {
            event.setState(EventState.CANCELED);
        }
    }

    private void updateEventFields(Event event, UpdateAdminEventDto updateRequest) {
        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }

        if (updateRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategory())
                    .orElseThrow(() -> new CategoryIdException(updateRequest.getCategory()));
            event.setCategory(category);
        }

        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }

        if (updateRequest.getEventDate() != null) {
            event.setEventDate(updateRequest.getEventDate());
        }

        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }

        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }

        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }

        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }

        if (updateRequest.getLocation() != null) {
            Location newLocation = locationMapper.toLocation(updateRequest.getLocation());
            event.setLocation(newLocation);
        }
    }
}
