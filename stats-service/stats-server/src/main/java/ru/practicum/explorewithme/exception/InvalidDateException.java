package ru.practicum.explorewithme.exception;

public class InvalidDateException extends IllegalArgumentException {
    public InvalidDateException(String s) {
        super(s);
    }
}
