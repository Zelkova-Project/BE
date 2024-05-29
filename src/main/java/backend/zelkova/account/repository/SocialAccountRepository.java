package backend.zelkova.account.repository;

import backend.zelkova.account.entity.Social;
import backend.zelkova.account.entity.SocialAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    Optional<SocialAccount> findBySocialIdAndSocial(String socialId, Social social);
}
