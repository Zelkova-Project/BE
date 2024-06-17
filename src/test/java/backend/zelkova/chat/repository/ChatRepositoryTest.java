package backend.zelkova.chat.repository;

import backend.zelkova.IntegrationTestSupport;
import backend.zelkova.account.entity.Account;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.chat.dto.response.LastChatResponse;
import backend.zelkova.chat.entity.Chat;
import backend.zelkova.helper.HardDeleteSupplier;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ChatRepositoryTest extends IntegrationTestSupport {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    HardDeleteSupplier hardDeleteSupplier;

    @AfterEach
    void tearDown() {
        hardDeleteSupplier.hardDelete(Chat.class, Account.class);
    }

    @Test
    @DisplayName("마지막 채팅 기록들 불러오기")
    void retrieveLastChats() throws Exception {

        // given
        Account A = accountRepository.save(new Account("A", "A", "A"));
        Account B = accountRepository.save(new Account("B", "B", "B"));
        Account C = accountRepository.save(new Account("C", "C", "C"));
        Account D = accountRepository.save(new Account("D", "D", "D"));

        Chat AtoB = chatRepository.save(new Chat(A, B, "A -> B"));

        chatRepository.save(new Chat(A, C, "A -> C"));
        Chat CtoA = chatRepository.save(new Chat(C, A, "C -> A"));

        chatRepository.save(new Chat(A, D, "A -> D"));
        chatRepository.save(new Chat(D, A, "D -> A"));
        Chat AtoD = chatRepository.save(new Chat(A, D, "A -> D"));

        // when
        List<LastChatResponse> lastChatResponses = chatRepository.retrieveLastChats(A.getId());

        // then
        Assertions.assertThat(lastChatResponses)
                .extracting(LastChatResponse::otherAccountId, LastChatResponse::chatId, LastChatResponse::lastMessage)
                .containsExactly(
                        new Tuple(D.getId(), AtoD.getId(), "A -> D"),
                        new Tuple(C.getId(), CtoA.getId(), "C -> A"),
                        new Tuple(B.getId(), AtoB.getId(), "A -> B")
                );
    }
}
