package ge.appointmentservice.dto.auth;

public record LoginRequest(
        String username,  //email
        String password
) {
}
