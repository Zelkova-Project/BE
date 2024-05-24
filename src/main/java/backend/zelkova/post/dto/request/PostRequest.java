package backend.zelkova.post.dto.request;

import backend.zelkova.post.model.Category;
import backend.zelkova.post.model.Visibility;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequest {

    @NotNull
    private Category category;

    @NotNull
    private Visibility visibility;

    @NotEmpty
    @Size(max = 255)
    private String title;

    @NotEmpty
    private String content;
}
