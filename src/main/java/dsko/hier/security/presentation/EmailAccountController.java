package dsko.hier.security.presentation;

import dsko.hier.global.response.APIResponse;
import dsko.hier.security.application.UserService;
import dsko.hier.security.dto.EmailSignUpDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/signup")
@RequiredArgsConstructor
public class EmailAccountController {
    //CREATE -> 회원 가입

    private final UserService userService;

    @PostMapping
    public APIResponse<UUID> signUp(@Validated @RequestBody EmailSignUpDto req) {
        UUID newUserId = userService.signUp(req);
        return APIResponse.success(newUserId);
    }

}
