package ru.practicum.explorewithme.service;

import ru.practicum.dto.ServiceHitDto;
import ru.practicum.dto.ServiceHitForListDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    ServiceHitDto saveHit(ServiceHitDto hitDto);

    List<ServiceHitForListDto> getHitStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
