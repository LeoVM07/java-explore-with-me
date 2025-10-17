package ru.practicum.explorewithme.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.dto.event.CreateEventDto;
import ru.practicum.explorewithme.dto.event.EventDto;
import ru.practicum.explorewithme.dto.event.EventInfoDto;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.utility.enums.EventState;

import java.time.LocalDateTime;


@Mapper(componentModel = "spring",
        uses = {CategoryMapper.class, UserMapper.class, LocationMapper.class},
        imports = {EventState.class, LocalDateTime.class})
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", expression = "java(EventState.PENDING)")
    @Mapping(target = "createdOn", expression = "java(LocalDateTime.now())")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "confirmedRequests", constant = "0L")
    Event toEvent(CreateEventDto newEventDto, User initiator, Category category);

    @Mapping(source = "category", target = "category")
    @Mapping(source = "location", target = "location")
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "state", expression = "java(event.getState())")
    EventDto toEventDto(Event event);

    @Mapping(target = "category", source = "category")
    @Mapping(target = "initiator", source = "initiator")
    @Mapping(target = "views", ignore = true)
    EventInfoDto toEventInfoDto(Event event);
}
