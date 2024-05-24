package backend.zelkova.account.repository;

import backend.zelkova.account.entity.AccountRole;
import backend.zelkova.account.entity.AccountRole.AccountRoleId;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRoleRepository extends JpaRepository<AccountRole, AccountRoleId> {

    Set<AccountRole> findByAccountId(Long accountId);
}
