package backend.zelkova.account.exception;

import org.springframework.security.core.AuthenticationException;

public class WrongRefererException extends AuthenticationException {

    public WrongRefererException() {
        super("허용되지 않는 Referer 헤더입니다.");
    }
}
