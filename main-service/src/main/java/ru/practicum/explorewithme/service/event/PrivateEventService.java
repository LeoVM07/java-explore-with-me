package ru.practicum.explorewithme.service.event;

import ru.practicum.explorewithme.dto.event.CreateEventDto;
import ru.practicum.explorewithme.dto.event.EventDto;
import ru.practicum.explorewithme.dto.event.EventInfoDto;
import ru.practicum.explorewithme.dto.event.UpdateEventDto;

import java.util.List;

public interface PrivateEventService {

    List<EventInfoDto> getEventsByUserId(Long userId, int from, int size);

    EventDto addEvent(Long userId, CreateEventDto eventDto);

    EventDto getEventById(Long userId, Long eventId);

    EventDto updateEvent(Long userId, Long eventId, UpdateEventDto updateEventDto);
}
