package ge.appointmentservice.service;


import ge.appointmentservice.dto.auth.AuthResponse;

public interface KeycloakAdminService {

    String createUser(
            String email,
            String firstName,
            String lastName,
            String password
    );

    void deleteUser(String keycloakId);

    void assignRole(
            String keycloakId,
            String roleName
    );

    void removeRole(
            String keycloakId,
            String roleName
    );

    AuthResponse login(
            String username,
            String password
    );

    void updateUserEmail(
            String keycloakId,
            String newEmail

    );


}
