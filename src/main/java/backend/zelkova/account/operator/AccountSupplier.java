package backend.zelkova.account.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.NormalAccount;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.account.repository.NormalAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class AccountSupplier {

    private final AccountRepository accountRepository;
    private final NormalAccountRepository normalAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public void supply(String loginId, String password, String name, String nickname, String email) {
        String encodedPassword = passwordEncoder.encode(password);
        Account account = new Account(name, nickname, email);
        accountRepository.save(account);
        NormalAccount normalAccount = new NormalAccount(account, loginId, encodedPassword);
        normalAccountRepository.save(normalAccount);
    }
}
