package ru.practicum.explorewithme.utility.enums;

import lombok.Getter;

@Getter
public enum StateAction {

    CANCEL_REVIEW("CANCEL_REVIEW"),
    SEND_TO_REVIEW("SEND_TO_REVIEW"),
    REJECT_EVENT("REJECT_EVENT"),
    PUBLISH_EVENT("PUBLISH_EVENT");

    private final String stateAction;

    StateAction(String stateAction) {
        this.stateAction = stateAction;
    }
}
