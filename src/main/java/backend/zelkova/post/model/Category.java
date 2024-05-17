package backend.zelkova.post.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    HOME_COMMUNICATION(true),
    RECRUIT(true),
    NOTICE(true),
    BOARD(false);

    private final boolean requiredRole;
}
