package backend.zelkova.chat.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.chat.dto.response.ChatroomResponse;
import backend.zelkova.chat.entity.ChatroomAccount;
import backend.zelkova.chat.repository.ChatroomAccountRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountChatroomReader {

    private final ChatroomAccountRepository chatroomAccountRepository;

    public Set<Account> findAccountsInChatroom(Long chatroomId) {
        return chatroomAccountRepository.findAllById_ChatroomId(chatroomId)
                .stream()
                .map(ChatroomAccount::getAccount)
                .collect(Collectors.toSet());
    }

    public List<ChatroomResponse> findAccountChatrooms(Long accountId) {
        return chatroomAccountRepository.retrieveAccountChatroomsWithLastChat(accountId);
    }
}
