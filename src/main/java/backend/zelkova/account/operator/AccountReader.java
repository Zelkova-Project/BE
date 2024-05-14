package backend.zelkova.account.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
public class AccountReader {

    private final AccountRepository accountRepository;

    public Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.NOTFOUND));
    }

    public Account findAccountByLoginId(String loginId) {
        return accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException(loginId + "을 찾을 수 없음"));
    }
}
