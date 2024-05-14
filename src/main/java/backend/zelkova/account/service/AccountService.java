package backend.zelkova.account.service;

import backend.zelkova.account.operator.AccountSupplier;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountSupplier accountSupplier;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void signup(String loginId, String password, String name, String nickname, String email) {
        accountSupplier.supply(loginId, password, name, nickname, email);
    }

    public void doLogin(String loginId, String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token =
                UsernamePasswordAuthenticationToken.unauthenticated(loginId, password);

        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
    }

    public void doLogout(HttpSession session) {
        SecurityContextHolder.clearContext();

        if (Objects.nonNull(session)) {
            session.invalidate();
        }
    }
}
