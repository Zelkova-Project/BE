package backend.zelkova.account.dto.response;

public enum LoginFailureCode {
    NOT_EXIST_LOGIN_ID,
    WRONG_PASSWORD,
    WRONG_REFERER_HEADER,
    ACCOUNT_PROBLEM;
}
