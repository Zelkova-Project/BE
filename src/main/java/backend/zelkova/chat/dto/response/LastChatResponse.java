package backend.zelkova.chat.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record LastChatResponse(Long otherAccountId, Long chatId, String lastMessage) {

    @QueryProjection
    public LastChatResponse {
    }
}
