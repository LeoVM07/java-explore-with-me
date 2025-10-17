package ru.practicum.explorewithme.controller.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.CreateCommentDto;
import ru.practicum.explorewithme.service.comment.CommentService;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@Validated
public class PrivateCommentController {

    private final CommentService commentService;

    @Autowired
    public PrivateCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@PathVariable("eventId") @NotNull @Positive Long eventId,
                                                    @PathVariable("userId") @NotNull @Positive Long userId,
                                                    @RequestBody @NotNull @Valid CreateCommentDto newComment) {
        return new ResponseEntity<>(commentService.addComment(eventId, userId, newComment),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("commentId") @NotNull @Positive Long commentId,
                                                    @PathVariable("eventId") @NotNull @Positive Long eventId,
                                                    @PathVariable("userId") @NotNull @Positive Long userId,
                                                    @RequestBody @Valid CreateCommentDto updateComment) {
        return new ResponseEntity<>(commentService.updateComment(commentId, eventId, userId, updateComment),
                HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("commentId") @NotNull @Positive Long commentId,
                              @PathVariable("eventId") @NotNull @Positive Long eventId,
                              @PathVariable("userId") @NotNull @Positive Long userId) {
        commentService.deleteComment(commentId, eventId, userId);
    }
}
