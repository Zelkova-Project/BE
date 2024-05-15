package backend.zelkova.account.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.AccountRole.AccountRoleId;
import backend.zelkova.account.entity.Role;
import backend.zelkova.account.repository.AccountRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class AccountRoleManager {

    private final AccountRoleRepository accountRoleRepository;

    public void delete(Account account, Role role) {
        accountRoleRepository.deleteById(new AccountRoleId(account.getId(), role));
    }
}
