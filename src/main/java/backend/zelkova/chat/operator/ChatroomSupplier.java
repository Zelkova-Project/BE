package backend.zelkova.chat.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.chat.entity.Chatroom;
import backend.zelkova.chat.entity.ChatroomAccount;
import backend.zelkova.chat.repository.ChatroomAccountRepository;
import backend.zelkova.chat.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatroomSupplier {

    private final ChatroomRepository chatroomRepository;
    private final ChatroomAccountRepository chatroomAccountRepository;
    private final AccountRepository accountRepository;

    public Chatroom supply(Long senderId, Long receiverId) {
        Chatroom chatroom = chatroomRepository.save(new Chatroom());

        Account sender = accountRepository.getReferenceById(senderId);
        Account receiver = accountRepository.getReferenceById(receiverId);

        chatroomAccountRepository.save(new ChatroomAccount(chatroom, sender));
        chatroomAccountRepository.save(new ChatroomAccount(chatroom, receiver));

        return chatroom;
    }
}
