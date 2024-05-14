package backend.zelkova.notice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeRequest {

    @NotEmpty
    @Max(value = 255)
    private String title;

    @NotEmpty
    private String content;
}
