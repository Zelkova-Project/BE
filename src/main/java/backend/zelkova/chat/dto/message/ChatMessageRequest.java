package backend.zelkova.chat.dto.message;

public record ChatMessageRequest(Long chatroomId, Long receiverId, String message) {
}
