package backend.zelkova.admin.dto.request;

import backend.zelkova.account.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemoveRoleRequest {

    @NotNull
    private Long accountId;

    @NotNull
    private Role role;
}
