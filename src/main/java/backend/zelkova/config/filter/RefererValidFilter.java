package backend.zelkova.config.filter;

import backend.zelkova.account.exception.WrongRefererException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class RefererValidFilter extends OncePerRequestFilter {

    private final List<String> allowReferer;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("/login".equals(request.getRequestURI())) {
            String referer = request.getHeader("Referer");

            if (Objects.isNull(referer) || !allowReferer.contains(referer)) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new WrongRefererException());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
