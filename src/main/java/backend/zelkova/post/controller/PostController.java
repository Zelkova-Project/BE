package backend.zelkova.post.controller;

import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.post.dto.request.PostDeleteRequest;
import backend.zelkova.post.dto.request.PostRequest;
import backend.zelkova.post.dto.request.PostUpdateRequest;
import backend.zelkova.post.dto.response.PostPreviewResponse;
import backend.zelkova.post.dto.response.PostResponse;
import backend.zelkova.post.service.PostService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/{noticeId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long noticeId) {
        return ResponseEntity.ok(postService.getPost(noticeId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> write(@AuthenticationPrincipal AccountDetail accountDetail,
                                      @RequestBody @Valid PostRequest postRequest) {

        Long noticeId = postService.write(accountDetail, postRequest.getTitle(), postRequest.getContent());
        return ResponseEntity.created(URI.create("/notices/" + noticeId))
                .build();
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> update(@AuthenticationPrincipal AccountDetail accountDetail,
                                       @RequestBody @Valid PostUpdateRequest postUpdateRequest) {

        postService.update(accountDetail, postUpdateRequest.getNoticeId(), postUpdateRequest.getTitle(),
                postUpdateRequest.getContent());

        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AccountDetail accountDetail,
                                       @RequestBody @Valid PostDeleteRequest postDeleteRequest) {

        postService.delete(accountDetail, postDeleteRequest.getNoticeId());

        return ResponseEntity.noContent()
                .build();
    }
}
