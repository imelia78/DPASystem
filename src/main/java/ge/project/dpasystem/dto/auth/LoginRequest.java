package ge.project.dpasystem.dto.auth;

public record LoginRequest(
        String username,  //email
        String password
) {
}
