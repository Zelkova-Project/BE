package backend.zelkova.account.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.NormalAccount;
import backend.zelkova.account.entity.Role;
import backend.zelkova.account.entity.SocialAccount;
import backend.zelkova.account.model.AccountDetail;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDetailSupplier {

    public AccountDetail supply(NormalAccount normalAccount, Set<Role> roles) {
        Account account = normalAccount.getAccount();
        return new AccountDetail(account.getId(), normalAccount.getLoginId(), normalAccount.getPassword(),
                account.getName(), roles);
    }

    public AccountDetail supply(SocialAccount socialAccount, Set<Role> roles, Map<String, Object> attributes) {
        Account account = socialAccount.getAccount();
        return new AccountDetail(account.getId(), account.getName(), roles, attributes);
    }
}
