package ru.practicum.explorewithme.exception;

public class InvalidEventTimeException extends RuntimeException {
    public InvalidEventTimeException() {
        super("Время нового события должно быть назначено более, чем через два часа от текущего времени");
    }
}
