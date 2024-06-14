package backend.zelkova.chat.repository;

import static backend.zelkova.chat.entity.QChat.chat;
import static backend.zelkova.chat.entity.QChatroom.chatroom;
import static backend.zelkova.chat.entity.QChatroomAccount.chatroomAccount;

import backend.zelkova.chat.dto.response.ChatroomResponse;
import backend.zelkova.chat.dto.response.QChatroomResponse;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatroomAccountRepositoryImpl implements ChatroomAccountRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<ChatroomResponse> retrieveAccountChatroomsWithLastChat(Long accountId) {
        return jpaQueryFactory
                .select(new QChatroomResponse(chatroom.id, chat.content))
                .from(chatroomAccount)

                .innerJoin(chatroomAccount.chatroom)

                .innerJoin(chat)
                .on(chat.id.eq(JPAExpressions
                        .select(chat.id.max())
                        .from(chat)
                        .where(chat.chatroom.eq(chatroom))
                ))

                .where(chatroomAccount.account.id.eq(accountId))

                .fetch();
    }

}
