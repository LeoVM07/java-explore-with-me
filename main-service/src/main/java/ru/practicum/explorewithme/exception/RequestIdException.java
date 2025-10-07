package ru.practicum.explorewithme.exception;

public class RequestIdException extends RuntimeException {
    public RequestIdException(Long requestId) {
        super(String.format("Запрос с id %d не найден", requestId));
    }
}
