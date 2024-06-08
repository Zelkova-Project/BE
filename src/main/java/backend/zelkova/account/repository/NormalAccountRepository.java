package backend.zelkova.account.repository;

import backend.zelkova.account.entity.NormalAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NormalAccountRepository extends JpaRepository<NormalAccount, Long> {
    Optional<NormalAccount> findByLoginId(String loginId);
}
