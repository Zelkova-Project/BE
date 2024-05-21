package backend.zelkova.account.entity;

import jakarta.persistence.Entity;
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
@Table(name = "normal_accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoAccount {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
