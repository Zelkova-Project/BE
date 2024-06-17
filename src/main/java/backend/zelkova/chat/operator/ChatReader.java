package backend.zelkova.chat.operator;

import backend.zelkova.chat.dto.response.LastChatResponse;
import backend.zelkova.chat.repository.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatReader {

    private final ChatRepository chatRepository;


    public List<LastChatResponse> findAccountChats(Long accountId) {
        return chatRepository.retrieveLastChats(accountId);
    }
}
