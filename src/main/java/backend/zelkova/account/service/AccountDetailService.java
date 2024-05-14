package backend.zelkova.account.service;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.Role;
import backend.zelkova.account.operator.AccountDetailSupplier;
import backend.zelkova.account.operator.AccountReader;
import backend.zelkova.account.operator.AccountRoleReader;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountDetailService implements UserDetailsService {

    private final AccountReader accountReader;
    private final AccountRoleReader accountRoleReader;
    private final AccountDetailSupplier accountDetailSupplier;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Account account = accountReader.findAccountByLoginId(loginId);
        Set<Role> roles = accountRoleReader.findRolesByAccountId(account.getId());
        return accountDetailSupplier.supply(account, roles);
    }
}
