package backend.zelkova.chat.repository;

import backend.zelkova.chat.dto.response.ChatroomResponse;
import java.util.List;

public interface ChatroomAccountRepositoryCustom {

    List<ChatroomResponse> retrieveAccountChatroomsWithLastChat(Long accountId);
}
