package ru.practicum.explorewithme.exception;

public class UserIdException extends RuntimeException {
    public UserIdException(long userId) {
        super(String.format("Пользователь с id %d не найден", userId));
    }
}
