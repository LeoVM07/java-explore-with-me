package ru.practicum.explorewithme.controller.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.EventDto;
import ru.practicum.explorewithme.dto.event.UpdateAdminEventDto;
import ru.practicum.explorewithme.service.event.AdminEventService;
import ru.practicum.explorewithme.utility.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.utility.Constant.DATE_TIME_PATTERN;

@RestController
@RequestMapping("/admin/events")
@Validated
public class AdminEventController {

    private final AdminEventService eventService;

    @Autowired
    public AdminEventController(AdminEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        return new ResponseEntity<>(eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from,
                size), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(@Positive @PathVariable("eventId") Long eventId,
                                                @Valid @RequestBody UpdateAdminEventDto eventDto) {
        return new ResponseEntity<>(eventService.updateEvent(eventId, eventDto), HttpStatus.OK);
    }
}
