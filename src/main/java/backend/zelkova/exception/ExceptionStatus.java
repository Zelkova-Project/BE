package backend.zelkova.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionStatus {
    NOTFOUND("요청하신 데이터가 존재하지 않습니다."),
    NO_PERMISSION("권한이 없습니다.", FORBIDDEN),
    FAIL_CONVERT("변환에 실패했습니다."),
    FAIL_ATTACHMENT_UPLOAD("첨부파일 업로드에 실패했습니다. 다시 시도해주세요.", INTERNAL_SERVER_ERROR),
    EXIST_LOGIN_ID("이미 존재하는 아이디입니다.");

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
