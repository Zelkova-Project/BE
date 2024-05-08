package backend.zelkova.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "account_roles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountRole {

    @EmbeddedId
    private AccountRoleId id;

    @MapsId("accountId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public AccountRole(Account account, Role role) {
        this.id = new AccountRoleId(account.getId(), role);
        this.account = account;
    }

    public Role getRole() {
        return this.id.getRole();
    }

    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class AccountRoleId implements Serializable {

        private Long accountId;

        @Enumerated(EnumType.STRING)
        @Column(name = "role_code", columnDefinition = "VARCHAR(20)")
        private Role role;
    }
}
