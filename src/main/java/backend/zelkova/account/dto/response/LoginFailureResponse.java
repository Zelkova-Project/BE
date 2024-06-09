package backend.zelkova.account.dto.response;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public record LoginFailureResponse(LoginFailureCode loginFailureCode) {

    public static LoginFailureResponse newInstance(Exception exception) {

        if (exception instanceof UsernameNotFoundException) {
            return new LoginFailureResponse(LoginFailureCode.NOT_EXIST_LOGIN_ID);
        }

        if (exception instanceof BadCredentialsException) {
            return new LoginFailureResponse(LoginFailureCode.WRONG_PASSWORD);
        }

        return new LoginFailureResponse(LoginFailureCode.ACCOUNT_PROBLEM);
    }
}
