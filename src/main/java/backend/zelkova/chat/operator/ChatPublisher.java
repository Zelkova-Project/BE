package backend.zelkova.chat.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.chat.dto.message.ChatMessage;
import backend.zelkova.chat.entity.Chat;
import backend.zelkova.chat.entity.Chatroom;
import backend.zelkova.chat.model.MessageDestination;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class ChatPublisher {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void publish(Set<Account> accountsInChatroom, Chat chat) {
        Chatroom chatroom = chat.getChatroom();
        sendMessage(chatroom, accountsInChatroom, chat.getAccount(), chat.getContent());
    }

    private void sendMessage(Chatroom chatroom, Set<Account> accountsInChatroom, Account account, String message) {
        accountsInChatroom.forEach(
                targetAccount -> sendMessageEachAccount(chatroom.getId(), targetAccount, account.getId(), message));
    }

    private void sendMessageEachAccount(Long chatroomId, Account targetAccount, Long publisherId, String message) {
        simpMessagingTemplate.convertAndSendToUser(targetAccount.getLoginId(), MessageDestination.MESSAGE,
                new ChatMessage(chatroomId, publisherId, message));
    }
}
