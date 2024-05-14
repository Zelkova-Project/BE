package backend.zelkova.notice.controller;

import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.notice.dto.request.NoticeRequest;
import backend.zelkova.notice.dto.request.NoticeUpdateRequest;
import backend.zelkova.notice.dto.response.NoticePreviewResponse;
import backend.zelkova.notice.dto.response.NoticeResponse;
import backend.zelkova.notice.service.NoticeService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<Page<NoticePreviewResponse>> getAllNotices(Pageable pageable) {
        return ResponseEntity.ok(noticeService.getNoticePreviews(pageable));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getNotice(noticeId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> write(@AuthenticationPrincipal AccountDetail accountDetail,
                                      @RequestBody @Valid NoticeRequest noticeRequest) {

        Long noticeId = noticeService.write(accountDetail, noticeRequest.getTitle(), noticeRequest.getContent());
        return ResponseEntity.created(URI.create("/notices/" + noticeId))
                .build();
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> update(@AuthenticationPrincipal AccountDetail accountDetail,
                                       @RequestBody @Valid NoticeUpdateRequest noticeUpdateRequest) {

        noticeService.update(accountDetail, noticeUpdateRequest.getNoticeId(), noticeUpdateRequest.getTitle(),
                noticeUpdateRequest.getContent());

        return ResponseEntity.noContent()
                .build();
    }
}
