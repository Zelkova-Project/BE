package backend.zelkova.notice.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.Objects;

public record NoticeResponse(Long no, String title, String content, LocalDateTime dateTime,
                             NoticePreviewResponse prev, NoticePreviewResponse next) {

    @QueryProjection
    public NoticeResponse(Long no, String title, String content, LocalDateTime dateTime, NoticePreviewResponse prev,
                          NoticePreviewResponse next) {
        this.no = no;
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.prev = requireNonNull(prev);
        this.next = requireNonNull(next);
    }

    private NoticePreviewResponse requireNonNull(NoticePreviewResponse noticePreviewResponse) {
        if (Objects.isNull(noticePreviewResponse.no())) {
            return null;
        }

        return noticePreviewResponse;
    }
}
