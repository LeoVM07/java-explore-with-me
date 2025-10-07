package ru.practicum.explorewithme.exception;

public class InitiatorIdException extends RuntimeException {
    public InitiatorIdException(Long userId, Long eventId) {
        super("Пользователь с id " + userId + " не является инициатором события c id " + eventId);
    }
}
