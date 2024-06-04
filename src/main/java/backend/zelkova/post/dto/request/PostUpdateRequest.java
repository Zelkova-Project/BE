package backend.zelkova.post.dto.request;

import backend.zelkova.post.model.Visibility;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdateRequest {

    @NotNull
    private Long postId;

    @NotNull
    private Visibility visibility;

    @NotEmpty
    @Size(max = 255)
    private String title;

    @NotEmpty
    private String content;

    private List<String> deleteAttachmentKeys;

    private List<MultipartFile> newAttachments;
}
