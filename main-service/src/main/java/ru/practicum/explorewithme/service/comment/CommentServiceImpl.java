package ru.practicum.explorewithme.service.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dal.CommentRepository;
import ru.practicum.explorewithme.dal.EventRepository;
import ru.practicum.explorewithme.dal.UserRepository;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.CreateCommentDto;
import ru.practicum.explorewithme.exception.CommentIdException;
import ru.practicum.explorewithme.exception.EventIdException;
import ru.practicum.explorewithme.exception.InvalidRequestDataException;
import ru.practicum.explorewithme.exception.UserIdException;
import ru.practicum.explorewithme.mapper.CommentMapper;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.utility.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, EventRepository eventRepository,
                              UserRepository userRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentDto addComment(Long eventId, Long userId, CreateCommentDto newComment) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventIdException(eventId));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new InvalidRequestDataException(String.format("Событие с id %d не опубликовано", eventId));
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UserIdException(userId));

        Comment comment = commentMapper.toComment(newComment);
        comment.setEvent(event);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        log.info("Добавление нового комментария: {}", savedComment);
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsByEventId(Long eventId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        log.info("Выведен список комментариев по параметрам: eventId: {}, from: {}, size: {}", eventId, from, size);
        return commentRepository.findAllByEventId(eventId, pageable)
                .stream()
                .map(commentMapper::toCommentDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentByIdAndEventId(Long id, Long eventId) {
        Comment comment = commentRepository.findByIdAndEventId(id, eventId)
                .orElseThrow(() -> new CommentIdException(id, eventId));

        return commentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteComment(Long id, Long eventId, Long userId) {
        if (!commentRepository.existsByIdAndEventIdAndAuthorId(id, eventId, userId)) {
            throw new CommentIdException(id, eventId, userId);
        }
        log.info("Удаление комментария с id: {} для события с id: {} пользователем с id: {}", id, eventId, userId);
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteComment(Long id, Long eventId) {
        if (!commentRepository.existsByIdAndEventId(id, eventId)) {
            throw new CommentIdException(id, eventId);
        }
        log.info("Удаление комментария с id: {} для события с id: {} администратором", id, eventId);
        commentRepository.deleteById(id);
    }

    @Override
    public CommentDto updateComment(Long id, Long eventId, Long userId, CreateCommentDto updateComment) {
        Comment comment = commentRepository.findByIdAndEventIdAndAuthorId(id, eventId, userId)
                .orElseThrow(() -> new CommentIdException(id, eventId, userId));

        comment.setMessage(updateComment.getMessage());
        comment.setUpdated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        log.info("Обновление комментария c id: {} для события с id: {} пользователем с id :{}", id, eventId, userId);
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    public CommentDto updateComment(Long id, Long eventId, CreateCommentDto updateComment) {
        Comment comment = commentRepository.findByIdAndEventId(id, eventId)
                .orElseThrow(() -> new CommentIdException(id, eventId));

        comment.setMessage(updateComment.getMessage());
        comment.setUpdated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        log.info("Обновление комментария c id: {} для события с id: {} администратором", id, eventId);
        return commentMapper.toCommentDto(savedComment);
    }
}
