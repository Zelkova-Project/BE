package backend.zelkova.config;

import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import java.security.Principal;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-zelkova")
                .addInterceptors(httpSessionHandshakeInterceptor())
                .setAllowedOriginPatterns("https://namu0005.or.kr")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue");
        registry.setApplicationDestinationPrefixes("/zelkova");
        registry.setUserDestinationPrefix("/zelkova/user");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AccountDetailWebsocketArgumentResolver());
    }

    @Bean
    HttpSessionHandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HttpSessionHandshakeInterceptor();
    }

    static class AccountDetailWebsocketArgumentResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            MethodParameter nestedParameter = parameter.nestedIfOptional();
            Class<?> paramType = nestedParameter.getNestedParameterType();
            return AccountDetail.class.isAssignableFrom(paramType);
        }

        @Override
        @Nullable
        public Object resolveArgument(MethodParameter parameter, Message<?> message) {
            Principal principal = SimpMessageHeaderAccessor.getUser(message.getHeaders());

            if (principal instanceof AbstractAuthenticationToken abstractAuthenticationToken) {
                return abstractAuthenticationToken.getPrincipal();
            }

            throw new CustomException(ExceptionStatus.FAIL_CONVERT);
        }
    }
}
