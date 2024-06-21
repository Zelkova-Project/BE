package backend.zelkova.chat.dto.response;

public record ChatResponse(Long chatId, Long senderId, String content) {
}
