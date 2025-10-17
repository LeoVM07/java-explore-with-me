package ru.practicum.explorewithme.exception;

public class CategoryIdException extends RuntimeException {
    public CategoryIdException(long categoryId) {
        super(String.format("Категория с id %d не найдена", categoryId));
    }
}
