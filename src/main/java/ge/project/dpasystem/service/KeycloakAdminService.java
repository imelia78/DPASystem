package ge.project.dpasystem.service;

import ge.project.dpasystem.dto.auth.AuthResponse;

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


}
