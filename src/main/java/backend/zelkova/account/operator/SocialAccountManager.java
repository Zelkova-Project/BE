package backend.zelkova.account.operator;

import backend.zelkova.account.entity.Social;
import backend.zelkova.account.entity.SocialAccount;
import backend.zelkova.account.repository.SocialAccountRepository;
import java.util.LinkedHashMap;
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

    @SuppressWarnings("unchecked")
    private SocialAccount supply(Social social, String socialId, Map<String, Object> attributes) {
        LinkedHashMap<String, String> properties = (LinkedHashMap<String, String>) attributes.get("properties");
        LinkedHashMap<String, String> kakaoAccount = (LinkedHashMap<String, String>) attributes.get("kakao_account");

        String name = properties.get("name");
        String nickname = properties.get("nickname");
        String email = kakaoAccount.get("email");

        return accountSupplier.supply(social, socialId, name, nickname, email);
    }
}
