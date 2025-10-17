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

import java.util.List;

@RestController
@RequestMapping("/admin/events/{eventId}/comments")
@Validated
public class AdminCommentController {

    private final CommentService commentService;

    @Autowired
    public AdminCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("commentId") @NotNull @Positive Long commentId,
                                                    @PathVariable("eventId") @NotNull @Positive Long eventId,
                                                    @RequestBody @Valid @NotNull CreateCommentDto updateComment) {
        return new ResponseEntity<>(commentService.updateComment(commentId, eventId, updateComment), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("commentId") @NotNull @Positive Long commentId,
                              @PathVariable("eventId") @NotNull @Positive Long eventId) {
        commentService.deleteComment(commentId, eventId);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllCommentsByEventId(
            @PathVariable("eventId") @NotNull @Positive Long eventId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return new ResponseEntity<>(commentService.getAllCommentsByEventId(eventId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentByIdAndEventId(
            @PathVariable("commentId") @NotNull @Positive Long commentId,
            @PathVariable("eventId") @NotNull @Positive Long eventId) {
        return new ResponseEntity<>(commentService.getCommentByIdAndEventId(commentId, eventId), HttpStatus.OK);
    }
}
