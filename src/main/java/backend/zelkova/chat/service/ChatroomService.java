package backend.zelkova.chat.service;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.operator.AccountReader;
import backend.zelkova.chat.dto.response.ChatroomResponse;
import backend.zelkova.chat.entity.Chat;
import backend.zelkova.chat.entity.Chatroom;
import backend.zelkova.chat.operator.AccountChatroomReader;
import backend.zelkova.chat.operator.ChatPublisher;
import backend.zelkova.chat.operator.ChatSupplier;
import backend.zelkova.chat.operator.ChatroomReader;
import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatroomService {

    private final AccountChatroomReader accountChatroomReader;
    private final AccountReader accountReader;
    private final ChatroomReader chatroomReader;
    private final ChatSupplier chatSupplier;
    private final ChatPublisher chatPublisher;

    public void sendMessage(Long chatroomId, Long senderId, Long receiverId, String message) {
        Set<Account> accountsInChatroom = accountChatroomReader.findAccountsInChatroom(chatroomId);

        if (!isParticipant(accountsInChatroom, senderId)) {
            throw new CustomException(ExceptionStatus.NOT_PARTICIPANT_ACCOUNT);
        }

        Chatroom chatroom = chatroomReader.getReferenceById(chatroomId);
        Account account = accountReader.getReferenceById(senderId);
        Chat chat = chatSupplier.supply(chatroom, account, message);

        chatPublisher.publish(accountsInChatroom, chat);
    }

    private boolean isParticipant(Set<Account> accountsInChatroom, Long accountId) {
        return accountsInChatroom.stream()
                .map(Account::getId)
                .anyMatch(accountIdInChatroom -> accountIdInChatroom.equals(accountId));
    }


    public List<ChatroomResponse> findAccountChatrooms(Long accountId) {
        return accountChatroomReader.findAccountChatrooms(accountId);
    }
}
