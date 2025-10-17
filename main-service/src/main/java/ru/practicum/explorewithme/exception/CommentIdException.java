package ru.practicum.explorewithme.exception;

public class CommentIdException extends RuntimeException {

    public CommentIdException(Long commentId) {
        super(String.format("Комментарий с id %d не найден", commentId));
    }

    public CommentIdException(Long commentId, Long eventId) {
        super(String.format("Комментарий с id %d для события с id %d не найден", commentId, eventId));
    }

    public CommentIdException(Long commentId, Long eventId, Long authorId) {
        super(String.format("Комментарий с id %d для события с id %d от пользователя с id %d не найден ",
                commentId, eventId, authorId));
    }
}
