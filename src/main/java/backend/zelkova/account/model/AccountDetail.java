package backend.zelkova.account.model;

import backend.zelkova.account.entity.Role;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AccountDetail implements UserDetails {

    @Getter
    private final Long accountId;
    private final String loginId;
    private final String password;
    private final Set<? extends GrantedAuthority> authorities;

    public AccountDetail(Long accountId, String loginId, String password, Set<? extends GrantedAuthority> authorities) {
        this.accountId = accountId;
        this.loginId = loginId;
        this.password = password;
        this.authorities = authorities;
    }

    public boolean hasAuthority(Role role) {
        return authorities.contains(role);
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
