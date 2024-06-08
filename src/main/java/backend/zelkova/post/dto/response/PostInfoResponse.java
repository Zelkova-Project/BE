package backend.zelkova.post.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.Objects;

public record PostInfoResponse(Long accountId, String accountName, Long no, String title, String content,
                               LocalDateTime dateTime, PostPreviewResponse prev, PostPreviewResponse next) {

    @QueryProjection
    public PostInfoResponse(Long accountId, String accountName, Long no, String title, String content,
                            LocalDateTime dateTime, PostPreviewResponse prev, PostPreviewResponse next) {
        this.accountId = accountId;
        this.accountName = accountName;
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
