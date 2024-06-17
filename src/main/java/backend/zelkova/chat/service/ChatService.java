package backend.zelkova.chat.service;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.operator.AccountReader;
import backend.zelkova.chat.dto.response.LastChatResponse;
import backend.zelkova.chat.operator.ChatPublisher;
import backend.zelkova.chat.operator.ChatReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final AccountReader accountReader;
    private final ChatPublisher chatPublisher;
    private final ChatReader chatReader;

    @Transactional
    public void sendChat(Long senderId, Long receiverId, String content) {
        Account sender = accountReader.getReferenceById(senderId);
        Account receiver = accountReader.getReferenceById(receiverId);
        chatPublisher.publish(sender, receiver, content);
    }

    public List<LastChatResponse> findAccountChats(Long accountId) {
        return chatReader.findAccountChats(accountId);
    }
}
