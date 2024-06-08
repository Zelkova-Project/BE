package backend.zelkova.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "social_accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialAccount {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_code", columnDefinition = "VARCHAR(20)")
    private Social social;

    @Column(columnDefinition = "VARCHAR(30)")
    private String socialId;

    public SocialAccount(Account account, Social social, String socialId) {
        this.id = account.getId();
        this.account = account;
        this.social = social;
        this.socialId = socialId;
    }
}
