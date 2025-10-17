package ru.practicum.explorewithme.controller.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.CreateEventDto;
import ru.practicum.explorewithme.dto.event.EventDto;
import ru.practicum.explorewithme.dto.event.EventInfoDto;
import ru.practicum.explorewithme.dto.event.UpdateEventDto;
import ru.practicum.explorewithme.service.event.PrivateEventService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
public class PrivateEventController {

    private final PrivateEventService eventService;

    @Autowired
    public PrivateEventController(PrivateEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventInfoDto>> getEventsByUserId(
            @PathVariable("userId") @Positive Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(eventService.getEventsByUserId(userId, from, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventDto> addEvent(@Positive @PathVariable("userId") Long userId,
                                             @Valid @RequestBody CreateEventDto eventDto) {
        return new ResponseEntity<>(eventService.addEvent(userId, eventDto), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@Positive @PathVariable("userId") Long userId,
                                                 @Positive @PathVariable("eventId") Long eventId) {
        return new ResponseEntity<>(eventService.getEventById(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(
            @Positive @PathVariable("userId") Long userId,
            @Positive @PathVariable("eventId") Long eventId,
            @Valid @RequestBody UpdateEventDto updateEventDto) {
        return new ResponseEntity<>(eventService.updateEvent(userId, eventId, updateEventDto), HttpStatus.OK);
    }
}
