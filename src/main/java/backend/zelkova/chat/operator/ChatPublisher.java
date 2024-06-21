package backend.zelkova.chat.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.chat.dto.response.ChatResponse;
import backend.zelkova.chat.entity.Chat;
import backend.zelkova.chat.model.MessageDestination;
import java.util.List;
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
        sendMessage(chat);
    }

    private void sendMessage(Chat chat) {
        Long senderId = chat.getSender().getId();
        Long receiverId = chat.getReceiver().getId();

        ChatResponse chatResponse = new ChatResponse(chat.getId(), senderId, chat.getContent());

        Set.copyOf(List.of(senderId, receiverId))
                .forEach(accountId -> sendMessageEachAccount(accountId, chatResponse));
    }

    private void sendMessageEachAccount(Long receiverId, ChatResponse chatResponse) {
        simpMessagingTemplate.convertAndSendToUser(receiverId.toString(), MessageDestination.MESSAGE, chatResponse);
    }
}
