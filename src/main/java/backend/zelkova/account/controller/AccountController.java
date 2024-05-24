package backend.zelkova.account.controller;


import backend.zelkova.account.dto.request.LoginRequestDto;
import backend.zelkova.account.dto.request.SignupRequestDto;
import backend.zelkova.account.service.AccountService;
import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequestDto requestDto) {

        String loginId = requestDto.getLoginId();
        String password = requestDto.getPassword();
        String name = requestDto.getName();
        String nickname = requestDto.getNickname();
        String email = requestDto.getEmail();

        accountService.signup(loginId, password, name, nickname, email);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletRequest request) {
        String loginId = requestDto.getLoginId();
        String password = requestDto.getPassword();

        try {
            accountService.doLogin(loginId, password, request);
            return ResponseEntity.ok().build();
        } catch (BadCredentialsException e) {
            throw new CustomException(ExceptionStatus.LOGIN_FAILURE);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        accountService.doLogout(session);
        return ResponseEntity.ok().build();
    }
}
