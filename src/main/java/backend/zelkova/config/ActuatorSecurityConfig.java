package backend.zelkova.config;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ActuatorSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    @Setter
    @Value("${actuator.user.id}")
    private String userId;

    @Setter
    @Value("${actuator.user.password}")
    private String password;

    @Bean
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(withDefaults());
        http.securityMatcher("/actuator/**");
        http.authorizeHttpRequests(request -> request.anyRequest().hasRole("ACTUATOR"));

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.userDetailsService(actuatorUserDetailService());

        return http.build();
    }

    private UserDetailsService actuatorUserDetailService() {
        UserDetails actuatorUser = User.withUsername(userId)
                .password(passwordEncoder.encode(password))
                .roles("ACTUATOR")
                .build();

        return new InMemoryUserDetailsManager(actuatorUser);
    }
}
