package backend.zelkova.account.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequestDto {

    @NotEmpty
    @Size(min = 4, max = 20)
    private String loginId;

    @NotEmpty
    private String password;

    @NotEmpty
    @Size(max = 10)
    private String name;

    @Size(max = 20)
    private String nickname;

    @Email
    @NotEmpty
    @Size(max = 255)
    private String email;
}
