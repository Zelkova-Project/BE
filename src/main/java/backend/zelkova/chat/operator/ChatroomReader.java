package backend.zelkova.chat.operator;

import backend.zelkova.chat.entity.Chatroom;
import backend.zelkova.chat.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatroomReader {

    private final ChatroomRepository chatroomRepository;

    public Chatroom getReferenceById(Long chatroomId) {
        return chatroomRepository.getReferenceById(chatroomId);
    }
}
