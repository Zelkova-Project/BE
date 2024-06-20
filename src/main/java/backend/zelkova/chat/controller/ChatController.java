package backend.zelkova.chat.controller;

import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.chat.dto.request.ChatRequest;
import backend.zelkova.chat.dto.response.LastChatResponse;
import backend.zelkova.chat.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @SubscribeMapping("/user/queue/message")
    public List<LastChatResponse> noticeChatroomInfo(AccountDetail accountDetail) {
        log.trace("구독");
        return chatService.findAccountChats(accountDetail.getAccountId());
    }

    @MessageMapping("/message")
    public void message(AccountDetail accountDetail, @Payload ChatRequest payload) {
        log.trace("메시지 발신");
        chatService.sendChat(accountDetail.getAccountId(), payload.receiverId(), payload.message());
    }
}
