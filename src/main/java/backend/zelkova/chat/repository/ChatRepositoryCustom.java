package backend.zelkova.chat.repository;

import backend.zelkova.chat.dto.response.LastChatResponse;
import java.util.List;

public interface ChatRepositoryCustom {

    List<LastChatResponse> retrieveLastChats(Long accountId);
}
