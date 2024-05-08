package backend.zelkova.account.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.AccountRole;
import backend.zelkova.account.entity.Role;
import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.account.repository.AccountRoleRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AccountDetailReader implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException(loginId + "을 찾을 수 없음"));

        Set<Role> roles = accountRoleRepository.findByAccountId(account.getId())
                .stream()
                .map(AccountRole::getRole)
                .collect(Collectors.toSet());

        return new AccountDetail(account.getId(), account.getLoginId(), account.getPassword(), roles);
    }
}
