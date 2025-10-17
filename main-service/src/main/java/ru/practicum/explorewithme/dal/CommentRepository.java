package ru.practicum.explorewithme.dal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventId(Long id, Pageable pageable);

    Optional<Comment> findByIdAndEventId(Long id, Long eventId);

    Optional<Comment> findByIdAndEventIdAndAuthorId(Long id, Long eventId, Long authorId);

    Boolean existsByIdAndEventId(Long id, Long eventId);

    Boolean existsByIdAndEventIdAndAuthorId(Long id, Long eventId, Long authorId);
}
