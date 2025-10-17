package ru.practicum.explorewithme.service.request;

import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.dto.request.RequestStatusUpdateDto;
import ru.practicum.explorewithme.dto.request.RequestUpdateResponseDto;

import java.util.List;

public interface PrivateRequestService {

    List<RequestDto> getUserRequests(Long userId);

    RequestDto addRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);

    List<RequestDto> getUserEventRequests(Long userId, Long eventId);

    RequestUpdateResponseDto updateRequestStatus(Long userId, Long eventId, RequestStatusUpdateDto statusUpdateDto);
}
