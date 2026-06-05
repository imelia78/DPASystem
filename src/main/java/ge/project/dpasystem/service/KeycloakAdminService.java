package ge.project.dpasystem.service;

public interface KeycloakAdminService {

    String createUser(
            String email,
            String firstName,
            String lastName,
            String password
    );

    void assignRole(
            String keycloakId,
            String roleName
    );

    void removeRole(
            String keycloakId,
            String roleName
    );
}
