package backend.zelkova.config;

import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.util.LinkedMultiValueMap;
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
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue");
        registry.setApplicationDestinationPrefixes("/zelkova");
        registry.setUserDestinationPrefix("/zelkova/user");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new CustomInterceptor());
    }

    static class CustomInterceptor implements ChannelInterceptor {

        private static final Pattern MESSAGE_PATTERN = Pattern.compile("^/zelkova/message/\\d+$");

        @Override
        @SuppressWarnings("unchecked")
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            SimpMessageType simpMessageType = (SimpMessageType) message.getHeaders().get("simpMessageType");

            if (simpMessageType != SimpMessageType.MESSAGE) {
                return message;
            }

            LinkedMultiValueMap<String, String> nativeHeaders =
                    (LinkedMultiValueMap<String, String>) message.getHeaders().get("nativeHeaders");

            if (Objects.isNull(nativeHeaders)) {
                throw new CustomException(ExceptionStatus.NO_PERMISSION);
            }

            Objects.requireNonNull(nativeHeaders.get("destination"))
                    .stream()
                    .filter(this::isMissMatch)
                    .findAny()
                    .ifPresent(destination -> {
                        throw new CustomException(ExceptionStatus.NO_PERMISSION);
                    });

            return message;
        }


        private boolean isMissMatch(String destination) {
            return !MESSAGE_PATTERN.matcher(destination).matches();
        }
    }
}
