package ru.practicum.explorewithme.exception;

public class EventIdException extends RuntimeException {
    public EventIdException(long eventId) {
        super(String.format("Событие с id %d не найдено", eventId));
    }
}
