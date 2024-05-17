package backend.zelkova.post.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record PostPreviewResponse(Long no, String title, LocalDateTime dateTime) {

    @QueryProjection
    public PostPreviewResponse {

    }
}
