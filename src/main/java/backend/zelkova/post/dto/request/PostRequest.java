package backend.zelkova.post.dto.request;

import backend.zelkova.post.model.Category;
import backend.zelkova.post.model.Visibility;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequest {

    @NotEmpty
    private Category category;

    @NotEmpty
    private Visibility visibility;

    @NotEmpty
    @Max(value = 255)
    private String title;

    @NotEmpty
    private String content;
}
