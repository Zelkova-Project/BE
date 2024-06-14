package backend.zelkova.account.service;

import backend.zelkova.account.entity.NormalAccount;
import backend.zelkova.account.entity.Role;
import backend.zelkova.account.entity.Social;
import backend.zelkova.account.entity.SocialAccount;
import backend.zelkova.account.operator.AccountDetailSupplier;
import backend.zelkova.account.operator.AccountReader;
import backend.zelkova.account.operator.AccountRoleReader;
import backend.zelkova.account.operator.SocialAccountManager;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountDetailService extends DefaultOAuth2UserService implements UserDetailsService {

    private final AccountReader accountReader;
    private final AccountRoleReader accountRoleReader;
    private final AccountDetailSupplier accountDetailSupplier;
    private final SocialAccountManager socialAccountManager;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        NormalAccount normalAccount = accountReader.findNormalAccountByLoginId(loginId);
        Set<Role> roles = accountRoleReader.findRolesByAccountId(normalAccount.getId());
        return accountDetailSupplier.supply(normalAccount, roles);
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Social social = findSocial(userRequest);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String socialId = attributes.get("id").toString();
        SocialAccount socialAccount = socialAccountManager.getOrCreateSocialAccount(social, socialId, attributes);
        Set<Role> roles = accountRoleReader.findRolesByAccountId(socialAccount.getId());
        return accountDetailSupplier.supply(socialAccount, roles, attributes);
    }

    private static Social findSocial(OAuth2UserRequest userRequest) {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String socialName = clientRegistration.getRegistrationId();
        return Social.valueOf(socialName.toUpperCase());
    }
}
