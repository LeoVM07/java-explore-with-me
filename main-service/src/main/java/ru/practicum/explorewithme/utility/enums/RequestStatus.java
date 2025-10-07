package ru.practicum.explorewithme.utility.enums;


import lombok.Getter;

@Getter
public enum RequestStatus {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    REJECTED("REJECTED"),
    CANCELED("CANCELED");

    private final String requestStatus;

    RequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
