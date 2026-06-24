package ge.appointmentservice.service;


import ge.appointmentservice.dto.auth.AuthResponse;
import ge.appointmentservice.dto.auth.LoginRequest;
import ge.appointmentservice.dto.auth.RegisterClientRequest;
import ge.appointmentservice.dto.auth.RegisterDoctorRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {


    private final DoctorService doctorService;
    private final ClientService clientService;

    private final KeycloakAdminService keycloakAdminService;

    public void registerDoctorRequest(RegisterDoctorRequest request) {

        var keycloakId = keycloakAdminService.createUser(request.email(), request.firstName(),
                request.lastName(), request.password());

        try {
            keycloakAdminService.assignRole(keycloakId, "dpasystem.DOCTOR_PENDING");

            log.info("Doctor with id {} has been registered in KeyCloak!", keycloakId);

            doctorService.createDoctor(request, keycloakId);

        } catch (Exception e) {
            rollbackKeycloakUser(keycloakId);
            throw e;
        }
    }

    private void rollbackKeycloakUser(String keycloakId) {
        try {
            keycloakAdminService.deleteUser(keycloakId);
            log.warn("Keycloak user {} removed due to registration failure", keycloakId);
        } catch (Exception ex) {
            log.error("failed to rollback keycloak user {}: {}", keycloakId, ex.getMessage());
        }

    }

    public void registerClientRequest(RegisterClientRequest request) {

        String keycloakId = keycloakAdminService.createUser(request.email(), request.firstName(),
                request.lastName(), request.password());
        keycloakAdminService.assignRole(keycloakId, "dpasystem.CLIENT"); // хранится внутри базы данных keycloak

        log.info("Client with id {} has been registered in Keycloak!", keycloakId);

        clientService.createClient(request, keycloakId);


    }


    public AuthResponse login(LoginRequest request) {
        var response = keycloakAdminService.login(request.username(),
                request.password());
        log.info("User {} authenticated successfully!", request.username());
        return response;
    }
}

