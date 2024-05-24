package backend.zelkova.post.dto.request;

import backend.zelkova.post.model.Category;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostMoveRequest {

    @NotNull
    Long postId;

    @NotNull
    Category category;
}
