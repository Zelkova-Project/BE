package backend.zelkova.chat.controller;

import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.chat.dto.message.ChatMessageRequest;
import backend.zelkova.chat.dto.response.ChatroomResponse;
import backend.zelkova.chat.service.ChatroomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final ChatroomService chatroomService;

    @SubscribeMapping("/user/queue/message")
    public List<ChatroomResponse> noticeChatroomInfo(@AuthenticationPrincipal AccountDetail accountDetail) {
        log.trace("구독");
        return chatroomService.findAccountChatrooms(accountDetail.getAccountId());
    }

    @MessageMapping("/message")
    public void message(@AuthenticationPrincipal AccountDetail accountDetail, @Payload ChatMessageRequest payload) {
        log.trace("메시지 발신");
        chatroomService.sendMessage(payload.chatroomId(), accountDetail.getAccountId(), payload.receiverId(),
                payload.message());
    }

    @SendToUser("/queue/message")
    @MessageMapping("/message/test")
    public String messageTest(@AuthenticationPrincipal AccountDetail accountDetail, @Payload String payload) {
        log.trace("메시지 테스트");
        return "발신 유저는 " + accountDetail.getUsername() + "이며, 전송하신 메시지는 " + payload + "입니다.";
    }
}
