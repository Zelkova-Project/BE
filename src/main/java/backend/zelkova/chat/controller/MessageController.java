package backend.zelkova.chat.controller;

import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.chat.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final ChatroomService chatroomService;

    @MessageMapping("/message/{chatroomId}")
    public void message(@DestinationVariable Long chatroomId, @AuthenticationPrincipal AccountDetail accountDetail,
                        @Payload String payload) {
        chatroomService.sendMessage(chatroomId, accountDetail, payload);
    }

    @SubscribeMapping("/user/queue/message")
    public String noticeChatroomInfo() {
        return "TODO: 채팅방 정보와 해당 채팅방들의 마지막 채팅 정보입니다.";
    }
}
