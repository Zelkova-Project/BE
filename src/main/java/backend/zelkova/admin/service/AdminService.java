package backend.zelkova.admin.service;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.Role;
import backend.zelkova.account.operator.AccountReader;
import backend.zelkova.account.operator.AccountRoleManager;
import backend.zelkova.account.operator.AccountRoleSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AccountReader accountReader;
    private final AccountRoleSupplier accountRoleSupplier;
    private final AccountRoleManager accountRoleManager;

    public void grantRole(Long accountId, Role role) {
        Account account = accountReader.findAccountById(accountId);
        accountRoleSupplier.supply(account, role);
    }

    public void removeRole(Long accountId, Role role) {
        Account account = accountReader.findAccountById(accountId);
        accountRoleManager.delete(account, role);
    }
}
