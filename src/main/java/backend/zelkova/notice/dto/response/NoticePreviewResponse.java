package backend.zelkova.notice.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record NoticePreviewResponse(Long no, String title, LocalDateTime dateTime) {

    @QueryProjection
    public NoticePreviewResponse {

    }
}
