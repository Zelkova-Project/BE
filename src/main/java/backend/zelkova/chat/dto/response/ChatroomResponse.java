package backend.zelkova.chat.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record ChatroomResponse(Long chatroomId, String lastMessage) {

    @QueryProjection
    public ChatroomResponse {
    }
}
