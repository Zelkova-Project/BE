package backend.zelkova.account.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.NormalAccount;
import backend.zelkova.account.entity.Social;
import backend.zelkova.account.entity.SocialAccount;
import backend.zelkova.account.repository.NormalAccountRepository;
import backend.zelkova.account.repository.SocialAccountRepository;
import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class AccountSupplier {

    private final NormalAccountRepository normalAccountRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public NormalAccount supply(String loginId, String password, String name, String nickname, String email) {
        Optional<NormalAccount> optNormalAccount = normalAccountRepository.findByLoginId(loginId);

        if (optNormalAccount.isPresent()) {
            throw new CustomException(ExceptionStatus.EXIST_LOGIN_ID);
        }

        String encodedPassword = passwordEncoder.encode(password);
        NormalAccount normalAccount = new NormalAccount(new Account(name, nickname, email), loginId, encodedPassword);
        normalAccountRepository.save(normalAccount);

        return normalAccount;
    }

    public SocialAccount supply(Social social, String socialId, String name, String nickname, String email) {
        SocialAccount socialAccount = new SocialAccount(new Account(name, nickname, email), social, socialId);
        socialAccountRepository.save(socialAccount);

        return socialAccount;
    }
}
