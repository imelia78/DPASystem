package ge.appointmentservice.dto.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        Long expiresIn,
        String tokenType
) {
}
