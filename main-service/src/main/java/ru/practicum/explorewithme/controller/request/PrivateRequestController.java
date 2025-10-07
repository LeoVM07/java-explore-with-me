package ru.practicum.explorewithme.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.dto.request.RequestStatusUpdateDto;
import ru.practicum.explorewithme.dto.request.RequestUpdateResponseDto;
import ru.practicum.explorewithme.service.request.PrivateRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
public class PrivateRequestController {

    private final PrivateRequestService requestService;

    @Autowired
    public PrivateRequestController(PrivateRequestService requestService) {
        this.requestService = requestService;
    }


    @GetMapping("/events/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getUserEventRequests(@Positive @PathVariable("userId") Long userId,
                                                                 @Positive @PathVariable("eventId") Long eventId) {
        return new ResponseEntity<>(requestService.getUserEventRequests(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/events/{eventId}/requests")
    public ResponseEntity<RequestUpdateResponseDto> updateRequestStatus(@Positive @PathVariable("userId") Long userId,
                                                                        @Positive @PathVariable("eventId") Long eventId,
                                                                        @Valid @RequestBody RequestStatusUpdateDto statusUpdateDto) {
        return new ResponseEntity<>(requestService.updateRequestStatus(userId, eventId, statusUpdateDto), HttpStatus.OK);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<RequestDto>> getUserRequests(@Positive @PathVariable("userId") Long userId) {
        return new ResponseEntity<>(requestService.getUserRequests(userId), HttpStatus.OK);
    }

    @PostMapping("/requests")
    public ResponseEntity<RequestDto> addRequest(@Positive @NotNull @PathVariable("userId") Long userId,
                                                 @Positive @NotNull @RequestParam("eventId") Long eventId) {
        return new ResponseEntity<>(requestService.addRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelRequest(@Positive @PathVariable("userId") Long userId,
                                                    @Positive @PathVariable("requestId") Long requestId) {
        return new ResponseEntity<>(requestService.cancelRequest(userId, requestId), HttpStatus.OK);
    }
}
