package backend.zelkova.post.dto.request;

import backend.zelkova.post.model.Category;
import backend.zelkova.post.model.Visibility;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUploadRequest {

    @NotNull
    private Category category;

    @NotNull
    private Visibility visibility;

    @NotEmpty
    @Size(max = 255)
    private String title;

    @NotEmpty
    private String content;

    private List<MultipartFile> files;
}
