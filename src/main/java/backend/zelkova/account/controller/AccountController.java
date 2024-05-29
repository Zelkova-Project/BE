package backend.zelkova.account.controller;


import backend.zelkova.account.dto.request.SignupRequestDto;
import backend.zelkova.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
