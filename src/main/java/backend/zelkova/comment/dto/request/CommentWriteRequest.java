package backend.zelkova.comment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentWriteRequest {

    @NotNull
    private Long postId;

    @NotNull
    private String comment;
}
