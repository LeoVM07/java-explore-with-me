package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "requesterId", source = "requester.id")
    RequestDto toDto(Request request);
}
