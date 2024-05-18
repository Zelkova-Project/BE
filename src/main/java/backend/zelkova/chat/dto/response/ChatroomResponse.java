package backend.zelkova.chat.dto.response;

import backend.zelkova.chat.entity.Chatroom;
import lombok.Getter;

@Getter
public class ChatroomResponse {
    private final Long chatroomId;
    private final String chatroomName;

    public ChatroomResponse(Chatroom chatroom) {
        this.chatroomId = chatroom.getId();
        this.chatroomName = chatroom.getName();
    }
}
