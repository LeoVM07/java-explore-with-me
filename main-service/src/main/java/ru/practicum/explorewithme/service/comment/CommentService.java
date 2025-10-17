package ru.practicum.explorewithme.service.comment;

import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.CreateCommentDto;

import java.util.List;


public interface CommentService {

    CommentDto addComment(Long eventId, Long userId, CreateCommentDto newComment);

    List<CommentDto> getAllCommentsByEventId(Long eventId, int from, int size);

    CommentDto getCommentByIdAndEventId(Long id, Long eventId);

    void deleteComment(Long id, Long eventId, Long userId);

    void deleteComment(Long id, Long eventId);

    CommentDto updateComment(Long id, Long eventId, Long userId, CreateCommentDto updateComment);

    CommentDto updateComment(Long id, Long eventId, CreateCommentDto updateComment);
}
