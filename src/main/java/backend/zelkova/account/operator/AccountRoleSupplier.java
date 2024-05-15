package backend.zelkova.account.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.AccountRole;
import backend.zelkova.account.entity.Role;
import backend.zelkova.account.repository.AccountRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class AccountRoleSupplier {

    private final AccountRoleRepository accountRoleRepository;

    public void supply(Account account, Role role) {
        AccountRole accountRole = new AccountRole(account, role);
        accountRoleRepository.save(accountRole);
    }
}
