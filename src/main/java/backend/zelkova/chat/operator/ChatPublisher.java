package backend.zelkova.chat.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.chat.dto.response.ChatResponse;
import backend.zelkova.chat.entity.Chat;
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

    private final ChatSupplier chatSupplier;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void publish(Account sender, Account receiver, String content) {
        Chat chat = chatSupplier.supply(sender, receiver, content);
        sendMessage(chat.getId(), chat.getContent(), chat.getSender().getId(), chat.getReceiver().getId());
    }

    private void sendMessage(Long chatId, String content, Long... accountIds) {
        Set.of(accountIds).forEach(
                accountId -> sendMessageEachAccount(accountId, chatId, content));
    }

    private void sendMessageEachAccount(Long accountId, Long chatId, String content) {
        simpMessagingTemplate.convertAndSendToUser(accountId.toString(), MessageDestination.MESSAGE,
                new ChatResponse(chatId, content));
    }
}
