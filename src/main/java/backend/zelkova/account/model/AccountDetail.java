package backend.zelkova.account.model;

import backend.zelkova.account.entity.Role;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class AccountDetail implements UserDetails, OAuth2User {

    @Getter
    private final Long accountId;
    private final String loginId;
    private final String password;
    private final String name;
    private final Set<? extends GrantedAuthority> authorities;
    private final transient Map<String, Object> attributes;

    public AccountDetail(Long accountId, String loginId, String password, String name,
                         Set<? extends GrantedAuthority> authorities) {
        this(accountId, loginId, password, name, authorities, Map.of());
    }

    public AccountDetail(Long accountId, String name, Set<? extends GrantedAuthority> authorities,
                         Map<String, Object> attributes) {
        this(accountId, null, null, name, authorities, attributes);
    }

    public AccountDetail(Long accountId, String loginId, String password, String name,
                         Set<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.accountId = accountId;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    public boolean hasAuthority(Role role) {
        return authorities.contains(role);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableSet(authorities);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
