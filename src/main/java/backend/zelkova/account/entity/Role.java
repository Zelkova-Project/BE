package backend.zelkova.account.entity;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, MANAGER;


    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
