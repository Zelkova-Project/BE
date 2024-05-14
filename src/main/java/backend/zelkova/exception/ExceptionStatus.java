package backend.zelkova.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionStatus {
    NOTFOUND("요청하신 데이터가 존재하지 않습니다."),
    LOGIN_FAILURE("로그인에 실패했습니다.");

    private final String message;
    private final HttpStatus httpStatus;

    ExceptionStatus(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    ExceptionStatus(String message) {
        this(message, BAD_REQUEST);
    }
}
