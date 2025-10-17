package ru.practicum.explorewithme.exception;

public class UpdatePublishedEventException extends RuntimeException {
    public UpdatePublishedEventException() {
        super("Нельзя изменить данные уже опубликованного события");
    }
}
