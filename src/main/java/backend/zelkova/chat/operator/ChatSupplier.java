package backend.zelkova.chat.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.chat.entity.Chat;
import backend.zelkova.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class ChatSupplier {

    private final ChatRepository chatRepository;

    public Chat supply(Account sender, Account receiver, String content) {
        return chatRepository.save(new Chat(sender, receiver, content));
    }
}
