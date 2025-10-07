package ru.practicum.explorewithme.utility.enums;

import lombok.Getter;

@Getter
public enum EventState {

    PUBLISHED("PUBLISHED"),
    PENDING("PENDING"),
    CANCELED("CANCELED");


    private final String eventState;

    EventState(String eventState) {
        this.eventState = eventState;
    }
}
