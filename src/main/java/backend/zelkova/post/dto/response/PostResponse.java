package backend.zelkova.post.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.Objects;

public record PostResponse(Long no, String title, String content, LocalDateTime dateTime,
                           PostPreviewResponse prev, PostPreviewResponse next) {

    @QueryProjection
    public PostResponse(Long no, String title, String content, LocalDateTime dateTime, PostPreviewResponse prev,
                        PostPreviewResponse next) {
        this.no = no;
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.prev = requireNonNull(prev);
        this.next = requireNonNull(next);
    }

    private PostPreviewResponse requireNonNull(PostPreviewResponse postPreviewResponse) {
        if (Objects.isNull(postPreviewResponse.no())) {
            return null;
        }

        return postPreviewResponse;
    }
}
