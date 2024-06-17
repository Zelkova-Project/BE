package backend.zelkova.chat.repository;

import static backend.zelkova.chat.entity.QChat.chat;

import backend.zelkova.chat.dto.response.LastChatResponse;
import backend.zelkova.chat.dto.response.QLastChatResponse;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<LastChatResponse> retrieveLastChats(Long accountId) {

        NumberExpression<Long> otherAccountId = calculateOtherAccountId(accountId);

        return jpaQueryFactory
                .select(new QLastChatResponse(otherAccountId, chat.id, chat.content))
                .from(chat)
                .where(chat.id.in(lastChatId(accountId, otherAccountId)))
                .orderBy(chat.id.desc())
                .fetch();
    }

    private static NumberExpression<Long> calculateOtherAccountId(Long accountId) {
        return new CaseBuilder()
                .when(chat.sender.id.eq(accountId))
                .then(chat.receiver.id)
                .otherwise(chat.sender.id);
    }

    private JPQLQuery<Long> lastChatId(Long accountId, NumberExpression<Long> otherAccountId) {

        return JPAExpressions.select(chat.id.max())
                .from(chat)
                .where(chat.sender.id.eq(accountId).or(chat.receiver.id.eq(accountId)))
                .groupBy(otherAccountId);
    }
}
