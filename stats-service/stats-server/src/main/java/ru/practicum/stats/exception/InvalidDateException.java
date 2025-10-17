package ru.practicum.stats.exception;

public class InvalidDateException extends IllegalArgumentException {
    public InvalidDateException(String s) {
        super(s);
    }
}
