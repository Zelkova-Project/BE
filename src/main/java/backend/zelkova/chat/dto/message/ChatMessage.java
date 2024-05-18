package backend.zelkova.chat.dto.message;

public record ChatMessage(Long chatroomId, Long accountId, String message) {
}
