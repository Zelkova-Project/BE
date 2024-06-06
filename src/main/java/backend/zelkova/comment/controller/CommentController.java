package backend.zelkova.comment.controller;

import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.comment.dto.request.CommentUpdateRequest;
import backend.zelkova.comment.dto.request.CommentWriteRequest;
import backend.zelkova.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> write(@AuthenticationPrincipal AccountDetail accountDetail,
                                      @RequestBody @Valid CommentWriteRequest commentWriteRequest) {

        commentService.write(commentWriteRequest.getPostId(), accountDetail.getAccountId(),
                commentWriteRequest.getComment());

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> update(@AuthenticationPrincipal AccountDetail accountDetail,
                                       @RequestBody @Valid CommentUpdateRequest commentUpdateRequest) {

        commentService.update(commentUpdateRequest.getCommentId(), accountDetail.getAccountId(),
                commentUpdateRequest.getContent());

        return ResponseEntity.noContent()
                .build();
    }
}
