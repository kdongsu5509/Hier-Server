package dsko.hier.security.dto;

public record EmailSignUpDto(
        String email,
        String nickname,
        String password
) {
}
