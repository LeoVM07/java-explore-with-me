package ru.practicum.explorewithme.service.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.explorewithme.dto.event.EventDto;
import ru.practicum.explorewithme.dto.event.EventInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {

    List<EventInfoDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size,
                                 HttpServletRequest request);

    EventDto getEvent(Long eventId, HttpServletRequest request);
}
