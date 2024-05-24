package backend.zelkova.account.repository;

import backend.zelkova.IntegrationTestSupport;
import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.AccountRole;
import backend.zelkova.account.entity.AccountRole.AccountRoleId;
import backend.zelkova.account.entity.Role;
import backend.zelkova.helper.HardDeleteSupplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AccountRoleRepositoryTest extends IntegrationTestSupport {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountRoleRepository accountRoleRepository;
    @Autowired
    HardDeleteSupplier hardDeleteSupplier;

    @AfterEach
    void tearDown() {
        hardDeleteSupplier.hardDelete(AccountRole.class, Account.class);
    }

    @Test
    @DisplayName("권한 부여")
    void insert() throws Exception {

        // given
        Account account = new Account("loginId", "password", "name", "nickname", "eora21@naver.com");
        accountRepository.save(account);

        accountRoleRepository.save(new AccountRole(account, Role.MANAGER));

        // when
        AccountRole findAccountRole = accountRoleRepository.findById(new AccountRoleId(account.getId(), Role.MANAGER))
                .orElseThrow();

        // then
        Assertions.assertThat(findAccountRole.getAccount().getId())
                .isEqualTo(account.getId());

        Assertions.assertThat(findAccountRole.getRole())
                .isEqualTo(Role.MANAGER);
    }
}
