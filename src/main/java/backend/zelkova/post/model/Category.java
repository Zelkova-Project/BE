package backend.zelkova.post.model;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {
    HOME_COMMUNICATION(true),
    RECRUIT(true),
    NOTICE(true),
    BOARD(false);

    private final boolean requiredRole;
}
