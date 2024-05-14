package backend.zelkova.account.operator;

import backend.zelkova.account.model.AccountDetail;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AccountDetailReader {

    public Optional<AccountDetail> getSessionAccountDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication)) {
            return Optional.empty();
        }

        AccountDetail accountDetail = (AccountDetail) authentication.getPrincipal();
        return Optional.of(accountDetail);
    }
}
