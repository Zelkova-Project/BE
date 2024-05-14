package backend.zelkova.notice.controller;

import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.notice.dto.request.NoticeRequest;
import backend.zelkova.notice.service.NoticeService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> write(@AuthenticationPrincipal AccountDetail accountDetail,
                                      @RequestBody @Valid NoticeRequest noticeRequest) {

        Long noticeId = noticeService.write(accountDetail, noticeRequest.getTitle(), noticeRequest.getContent());
        return ResponseEntity.created(URI.create("/notices/" + noticeId))
                .build();
    }
}
