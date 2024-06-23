package backend.zelkova.account.operator;

import backend.zelkova.IntegrationTestSupport;
import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.NormalAccount;
import backend.zelkova.account.repository.NormalAccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AccountReaderTest extends IntegrationTestSupport {

    @Autowired
    AccountReader accountReader;

    @Autowired
    NormalAccountRepository normalAccountRepository;

    @AfterEach
    void tearDown() {
        normalAccountRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("중복된 loginId 존재")
    void duplicateLoginId() throws Exception {

        // given
        normalAccountRepository.save(
                new NormalAccount(new Account("name", "nickname", "email@email.com"), "loginId", "password"));

        // when
        boolean possibleLoginId = accountReader.isPossibleLoginId("loginId");

        // then
        Assertions.assertThat(possibleLoginId).isFalse();
    }

    @Test
    @DisplayName("가능한 loginId 존재")
    void possibleLoginId() throws Exception {

        // given

        // when
        boolean possibleLoginId = accountReader.isPossibleLoginId("loginId");

        // then
        Assertions.assertThat(possibleLoginId).isTrue();
    }
}
