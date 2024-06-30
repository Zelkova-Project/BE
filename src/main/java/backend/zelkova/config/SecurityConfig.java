package backend.zelkova.config;

import backend.zelkova.account.dto.response.LoginFailureResponse;
import backend.zelkova.config.filter.RefererValidFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    @Value("${login.referer.allow}")
    private List<String> allowLoginRefererHeader;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(httpSecurityCsrfConfigurer -> {
            httpSecurityCsrfConfigurer.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler());
            httpSecurityCsrfConfigurer.ignoringRequestMatchers("/signup", "/login/**", "/logout", "/ws-zelkova/**");
            httpSecurityCsrfConfigurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        });

        http.httpBasic(AbstractHttpConfigurer::disable);

        http.addFilterBefore(new RefererValidFilter(allowLoginRefererHeader, authenticationFailureHandler()),
                UsernamePasswordAuthenticationFilter.class);

        http.formLogin(formConfig -> {
            formConfig.usernameParameter("loginId");
            formConfig.successHandler(authenticationSuccessHandler());
            formConfig.failureHandler(authenticationFailureHandler());
        });

        http.logout(logoutConfig -> logoutConfig.logoutSuccessHandler(logoutSuccessHandler()));

        http.oauth2Login(oauth2Config -> {
            oauth2Config.successHandler(authenticationSuccessHandler());
            oauth2Config.failureHandler(authenticationFailureHandler());
        });

        http.exceptionHandling(exceptionHandleConfig -> {
            exceptionHandleConfig.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
            exceptionHandleConfig.accessDeniedHandler((request, response, accessDeniedException) ->
                    response.sendError(HttpServletResponse.SC_NOT_FOUND));
        });

        http.authorizeHttpRequests(authorizeRequests -> {
            authorizeRequests.requestMatchers("/signup", "/login/**", "/oauth2/**")
                    .permitAll();
            authorizeRequests.requestMatchers(HttpMethod.GET, "/posts/**", "/accounts/**")
                    .permitAll();
            authorizeRequests.requestMatchers("/roles/**", "/swagger-ui/**")
                    .hasRole("ADMIN");

            authorizeRequests.requestMatchers("/docs/**")
                    .hasAnyRole("ADMIN", "MANAGER");

            authorizeRequests.anyRequest()
                    .authenticated();
        });

        return http.build();
    }

    @Bean
    @Profile({"release, master"})
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://namu0005.or.kr"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,
                                                               UserDetailsService userDetailsService) {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);

        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

            if (Objects.nonNull(csrfToken.getHeaderName())) {
                response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
            }

            response.setStatus(HttpServletResponse.SC_OK);
        };
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK);
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(LoginFailureResponse.newInstance(exception)));
        };
    }
}
