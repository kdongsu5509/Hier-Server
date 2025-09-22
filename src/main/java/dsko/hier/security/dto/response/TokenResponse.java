package dsko.hier.security.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
