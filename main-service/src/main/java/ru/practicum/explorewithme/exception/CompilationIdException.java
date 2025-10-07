package ru.practicum.explorewithme.exception;

public class CompilationIdException extends RuntimeException {
    public CompilationIdException(Long compilationId) {
        super(String.format("Подборки с id %d не найдено", compilationId));
    }
}
