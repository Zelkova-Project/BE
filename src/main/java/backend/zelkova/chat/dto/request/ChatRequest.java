package backend.zelkova.chat.dto.request;

public record ChatRequest(Long receiverId, String message) {
}
