package backend.zelkova.post.dto.request;

import backend.zelkova.post.model.Visibility;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdateRequest {

    @NotNull
    private Long noticeId;

    @NotNull
    private Visibility visibility;

    @NotEmpty
    @Max(value = 255)
    private String title;

    @NotEmpty
    private String content;
}
