package backend.zelkova.chat.operator;

import backend.zelkova.chat.entity.Chatroom;
import backend.zelkova.chat.repository.ChatroomRepository;
import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatroomManager {

    private final ChatroomSupplier chatroomSupplier;
    private final ChatroomRepository chatroomRepository;

    public Chatroom getOrCreate(Long chatroomId, Long senderId, Long receiverId) {
        if (Objects.isNull(chatroomId)) {
            return chatroomSupplier.supply(senderId, receiverId);
        }

        return chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.NOTFOUND));
    }
}
