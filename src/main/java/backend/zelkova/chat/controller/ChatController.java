package backend.zelkova.chat.controller;

import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.chat.dto.request.ChatRequest;
import backend.zelkova.chat.dto.response.LastChatResponse;
import backend.zelkova.chat.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @SubscribeMapping("/user/queue/message")
    public List<LastChatResponse> noticeChatroomInfo(AccountDetail accountDetail) {
        return chatService.findAccountChats(accountDetail.getAccountId());
    }

    @MessageMapping("/message")
    public void message(AccountDetail accountDetail, @Payload ChatRequest payload) {
        chatService.sendChat(accountDetail.getAccountId(), payload.receiverId(), payload.message());
    }
}
