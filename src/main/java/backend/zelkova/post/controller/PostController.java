package backend.zelkova.post.controller;

import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.post.dto.request.PostDeleteRequest;
import backend.zelkova.post.dto.request.PostMoveRequest;
import backend.zelkova.post.dto.request.PostUpdateRequest;
import backend.zelkova.post.dto.request.PostUploadRequest;
import backend.zelkova.post.dto.response.PostPreviewResponse;
import backend.zelkova.post.dto.response.PostResponse;
import backend.zelkova.post.service.PostService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostPreviewResponse>> getPostPreviews(Pageable pageable) {
        return ResponseEntity.ok(postService.getPostPreviews(pageable));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PostMapping
    public ResponseEntity<Void> write(@AuthenticationPrincipal AccountDetail accountDetail,
                                      @ModelAttribute @Valid PostUploadRequest postUploadRequest) {

        Long postId = postService.write(accountDetail.getAccountId(), postUploadRequest.getCategory(),
                postUploadRequest.getVisibility(), postUploadRequest.getTitle(), postUploadRequest.getContent(),
                postUploadRequest.getFiles());

        return ResponseEntity.created(URI.create("/posts/" + postId))
                .build();
    }

    @PatchMapping
    public ResponseEntity<Void> update(@AuthenticationPrincipal AccountDetail accountDetail,
                                       @RequestBody @Valid PostUpdateRequest postUpdateRequest) {

        postService.update(accountDetail.getAccountId(), postUpdateRequest.getPostId(),
                postUpdateRequest.getVisibility(), postUpdateRequest.getTitle(), postUpdateRequest.getContent(),
                postUpdateRequest.getDeleteAttachmentKeys(), postUpdateRequest.getNewAttachments());

        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/move")
    public ResponseEntity<Void> move(@AuthenticationPrincipal AccountDetail accountDetail,
                                     @RequestBody @Valid PostMoveRequest postMoveRequest) {

        postService.move(accountDetail.getAccountId(), postMoveRequest.getPostId(), postMoveRequest.getCategory());

        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AccountDetail accountDetail,
                                       @RequestBody @Valid PostDeleteRequest postDeleteRequest) {

        postService.delete(accountDetail.getAccountId(), postDeleteRequest.getPostId());

        return ResponseEntity.noContent()
                .build();
    }
}
