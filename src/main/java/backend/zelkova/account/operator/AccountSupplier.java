package backend.zelkova.account.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.NormalAccount;
import backend.zelkova.account.entity.Social;
import backend.zelkova.account.entity.SocialAccount;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.account.repository.NormalAccountRepository;
import backend.zelkova.account.repository.SocialAccountRepository;
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
    private final SocialAccountRepository socialAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public NormalAccount supply(String loginId, String password, String name, String nickname, String email) {
        String encodedPassword = passwordEncoder.encode(password);
        Account account = createAccount(name, nickname, email);
        NormalAccount normalAccount = new NormalAccount(account, loginId, encodedPassword);
        normalAccountRepository.save(normalAccount);

        return normalAccount;
    }

    private Account createAccount(String name, String nickname, String email) {
        Account account = new Account(name, nickname, email);
        return accountRepository.save(account);
    }

    public SocialAccount supply(Social social, String socialId, String name, String nickname, String email) {
        Account account = createAccount(name, nickname, email);
        SocialAccount socialAccount = new SocialAccount(account, social, socialId);
        socialAccountRepository.save(socialAccount);

        return socialAccount;
    }
}
