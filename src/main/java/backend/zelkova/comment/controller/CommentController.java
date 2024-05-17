package backend.zelkova.comment.controller;

import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.comment.dto.request.CommentRequest;
import backend.zelkova.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Void> addComment(@AuthenticationPrincipal AccountDetail accountDetail,
                                           @RequestBody @Valid CommentRequest commentRequest) {

        commentService.write(commentRequest.getPostId(), accountDetail.getAccountId(), commentRequest.getComment());

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
