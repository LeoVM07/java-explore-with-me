package ru.practicum.explorewithme.service.event;

import ru.practicum.explorewithme.dto.event.EventDto;
import ru.practicum.explorewithme.dto.event.UpdateAdminEventDto;
import ru.practicum.explorewithme.utility.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {

    List<EventDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventDto updateEvent(Long eventId, UpdateAdminEventDto eventDto);
}
