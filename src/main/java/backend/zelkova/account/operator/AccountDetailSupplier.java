package backend.zelkova.account.operator;

import backend.zelkova.account.entity.NormalAccount;
import backend.zelkova.account.entity.Role;
import backend.zelkova.account.model.AccountDetail;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDetailSupplier {

    public AccountDetail supply(NormalAccount normalAccount, Set<Role> roles) {
        return new AccountDetail(normalAccount.getId(), normalAccount.getLoginId(), normalAccount.getPassword(), roles);
    }
}
