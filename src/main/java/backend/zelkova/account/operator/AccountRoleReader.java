package backend.zelkova.account.operator;

import backend.zelkova.account.entity.AccountRole;
import backend.zelkova.account.entity.Role;
import backend.zelkova.account.repository.AccountRoleRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
public class AccountRoleReader {

    private final AccountRoleRepository accountRoleRepository;

    public Set<Role> findRolesByAccountId(Long accountId) {
        return accountRoleRepository.findByAccountId(accountId)
                .stream()
                .map(AccountRole::getRole)
                .collect(Collectors.toSet());
    }
}
