package backend.zelkova.chat.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.chat.entity.Chat;
import backend.zelkova.chat.entity.Chatroom;
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

    public Chat supply(Chatroom chatroom, Account account, String content) {
        return chatRepository.save(new Chat(chatroom, account, content));
    }
}
