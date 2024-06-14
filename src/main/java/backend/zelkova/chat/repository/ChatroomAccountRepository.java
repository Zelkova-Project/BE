package backend.zelkova.chat.repository;

import backend.zelkova.chat.entity.ChatroomAccount;
import backend.zelkova.chat.entity.ChatroomAccount.ChatroomAccountId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomAccountRepository extends JpaRepository<ChatroomAccount, ChatroomAccountId>,
        ChatroomAccountRepositoryCustom {

    List<ChatroomAccount> findAllById_AccountId(Long accountId);

    List<ChatroomAccount> findAllById_ChatroomId(Long chatroomId);
}
