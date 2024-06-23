package backend.zelkova.account.service;

import backend.zelkova.account.operator.AccountReader;
import backend.zelkova.account.operator.AccountSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountSupplier accountSupplier;
    private final AccountReader accountReader;

    @Transactional
    public void signup(String loginId, String password, String name, String nickname, String email) {
        accountSupplier.supply(loginId, password, name, nickname, email);
    }

    public boolean isPossibleLoginId(String loginId) {
        return accountReader.isPossibleLoginId(loginId);
    }
}
