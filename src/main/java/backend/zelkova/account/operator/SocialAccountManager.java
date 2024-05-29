package backend.zelkova.account.operator;

import backend.zelkova.account.entity.Social;
import backend.zelkova.account.entity.SocialAccount;
import backend.zelkova.account.repository.SocialAccountRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class SocialAccountManager {

    private final AccountSupplier accountSupplier;
    private final SocialAccountRepository socialAccountRepository;

    public SocialAccount getOrCreateSocialAccount(Social social, String socialId, Map<String, Object> attributes) {
        return socialAccountRepository.findBySocialIdAndSocial(socialId, social)
                .orElseGet(() -> this.supply(social, socialId, attributes));
    }

    private SocialAccount supply(Social social, String socialId, Map<String, Object> attributes) {
        String name = (String) attributes.get("name");
        String nickname = (String) attributes.get("nickname");
        String email = (String) attributes.get("email");
        return accountSupplier.supply(social, socialId, name, nickname, email);
    }
}
