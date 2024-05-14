package backend.zelkova.notice.operator;

import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import backend.zelkova.notice.dto.response.NoticePreviewResponse;
import backend.zelkova.notice.dto.response.NoticeResponse;
import backend.zelkova.notice.entity.Notice;
import backend.zelkova.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoticeReader {

    private final NoticeRepository noticeRepository;

    public Page<NoticePreviewResponse> findAll(Pageable pageable) {
        return noticeRepository.retrieveAllNoticesResponses(pageable);
    }

    public NoticeResponse findNoticeResponseByNoticeId(Long noticeId) {
        return noticeRepository.retrieveNoticeResponse(noticeId);
    }

    public Notice findById(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.NOTFOUND));
    }
}
