package backend.zelkova.post.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.AccountRole;
import backend.zelkova.account.entity.Role;
import backend.zelkova.account.repository.AccountRoleRepository;
import backend.zelkova.post.entity.Post;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostPermissionValidator {

    private final AccountRoleRepository accountRoleRepository;

    public boolean hasPermission(Post post, Long accountId) {
        return isOwner(post, accountId) || hasRole(accountId);
    }

    public boolean isOwner(Post post, Long accountId) {
        Account account = post.getAccount();
        return account.getId().equals(accountId);
    }

    public boolean hasRole(Long accountId) {
        Set<Role> accountRoles = accountRoleRepository.findByAccountId(accountId)
                .stream()
                .map(AccountRole::getRole)
                .collect(Collectors.toSet());

        return accountRoles.contains(Role.ADMIN) || accountRoles.contains(Role.MANAGER);
    }
}
