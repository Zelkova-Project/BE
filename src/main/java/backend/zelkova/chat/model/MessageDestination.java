package backend.zelkova.chat.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageDestination {

    public static final String MESSAGE = "/queue/message";
}
