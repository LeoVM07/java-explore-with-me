package ru.practicum.explorewithme.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.ServiceHitDto;
import ru.practicum.explorewithme.model.ServiceHit;

@UtilityClass
public class ServiceHitMapper {

    public static ServiceHitDto toDtoFromHit(ServiceHit hit) {
        return new ServiceHitDto(hit.getApp(), hit.getUri(), hit.getIp(), hit.getTimestamp());
    }

    public static ServiceHit toHit(ServiceHitDto hitDto) {
        return new ServiceHit(hitDto.getApp(), hitDto.getUri(), hitDto.getIp(), hitDto.getTimestamp());
    }
}
