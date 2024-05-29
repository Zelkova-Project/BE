package backend.zelkova.account.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "normal_accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NormalAccount {

    @Id
    private Long id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @NotBlank
    private String loginId;

    @NotBlank
    @Column(columnDefinition = "CHAR(60)")
    private String password;

    public NormalAccount(Account account, String loginId, String password) {
        this.id = account.getId();
        this.account = account;
        this.loginId = loginId;
        this.password = password;
    }
}
