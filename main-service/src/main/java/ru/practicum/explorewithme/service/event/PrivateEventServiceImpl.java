package ru.practicum.explorewithme.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dal.CategoryRepository;
import ru.practicum.explorewithme.dal.EventRepository;
import ru.practicum.explorewithme.dal.UserRepository;
import ru.practicum.explorewithme.dto.event.CreateEventDto;
import ru.practicum.explorewithme.dto.event.EventDto;
import ru.practicum.explorewithme.dto.event.EventInfoDto;
import ru.practicum.explorewithme.dto.event.UpdateEventDto;
import ru.practicum.explorewithme.exception.*;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.mapper.LocationMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.utility.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;

    @Autowired
    public PrivateEventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                                   CategoryRepository categoryRepository,
                                   EventMapper eventMapper, LocationMapper locationMapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.eventMapper = eventMapper;
        this.locationMapper = locationMapper;
    }

    @Override
    public List<EventInfoDto> getEventsByUserId(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new UserIdException(userId);
        }

        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.findByInitiatorIdOrderByEventDateDesc(userId, pageable)
                .stream()
                .map(eventMapper::toEventInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto addEvent(Long userId, CreateEventDto eventDto) {
        if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidEventTimeException();
        }

        User initiator = userRepository.findById(userId).orElseThrow(() -> new UserIdException(userId));
        Category category = checkCategory(eventDto.getCategoryId());

        Event newEvent = eventMapper.toEvent(eventDto, initiator, category);
        Event savedEvent = eventRepository.save(newEvent);

        log.info("Добавление нового события: {}", savedEvent);
        return eventMapper.toEventDto(savedEvent);
    }

    @Override
    public EventDto getEventById(Long userId, Long eventId) {
        checkUser(userId);
        Event event = getEventWithInitiator(eventId, userId);
        log.info("Получение пользователем с id: {} информации о событии с id: {}", userId, eventId);
        return eventMapper.toEventDto(event);
    }

    @Override
    public EventDto updateEvent(Long userId, Long eventId, UpdateEventDto updateEventDto) {
        checkUser(userId);
        Event event = getEventWithInitiator(eventId, userId);

        if (event.getState() == EventState.PUBLISHED) {
            throw new UpdatePublishedEventException();
        }

        if (updateEventDto.getEventDate() != null &&
                updateEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidEventTimeException();
        }

        if (updateEventDto.getAnnotation() != null) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }
        if (updateEventDto.getCategoryId() != null) {
            Category category = checkCategory(updateEventDto.getCategoryId());
            event.setCategory(category);
        }

        if (updateEventDto.getDescription() != null) {
            event.setDescription(updateEventDto.getDescription());
        }

        if (updateEventDto.getLocation() != null) {
            event.setLocation(locationMapper.toLocation(updateEventDto.getLocation()));
        }
        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }
        if (updateEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }
        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }
        if (updateEventDto.getTitle() != null) {
            event.setTitle(updateEventDto.getTitle());
        }

        if (updateEventDto.getStateAction() != null) {
            switch (updateEventDto.getStateAction()) {
                case SEND_TO_REVIEW:
                    if (event.getState() == EventState.CANCELED) {
                        event.setState(EventState.PENDING);
                    }
                    break;
                case CANCEL_REVIEW:
                    if (event.getState() == EventState.PENDING) {
                        event.setState(EventState.CANCELED);
                    }
                    break;
            }
        }
        Event savedEvent = eventRepository.save(event);

        log.info("Пользователь с id: {} обновил событие: {}", userId, savedEvent);
        return eventMapper.toEventDto(savedEvent);
    }


    private void checkUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserIdException(userId);
        }
    }

    private Category checkCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryIdException(categoryId));
    }

    private Event getEventWithInitiator(Long eventId, Long initiatorId) {
        return eventRepository.findByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new EventIdException(eventId));
    }
}
