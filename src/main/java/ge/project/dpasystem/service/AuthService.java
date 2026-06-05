package ge.project.dpasystem.service;

import ge.project.dpasystem.dto.auth.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {


    private final RestTemplate restTemplate;
    private final DoctorService doctorService;

    private static final String SERVER_URL = "http://keycloak:8080";

    private static final String REALM  = "dpasystem";

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;


    private String tokenUrl() {
        return SERVER_URL +
                "/realms/" +
                REALM +
                "/protocol/openid-connect/token";
    }

    private String usersUrl() {
        return SERVER_URL +
                "/admin/realms/" +
                REALM +
                "/users";
    }

    private String realmRoleMappingsUrl(
            String keycloakUserId
    ) {
        return SERVER_URL
                + "/admin/realms/"
                + REALM
                + "/users/"
                + keycloakUserId
                + "/role-mappings/realm";
    }

    private String getAdminToken() {

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        var body = "grant_type=client_credentials"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret;

        var request = new HttpEntity<>(body, headers);

        var response = restTemplate.postForObject(
                tokenUrl(),
                request,
                TokenResponse.class
        );
        return Objects.requireNonNull(response).accessToken();
    }


    private void createClient(
            RegisterClientRequest request,
            String adminToken
    ) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        var credentials = Map.of(
                "type", "password",
                "value", request.password(),
                "temporary", false
        );

        var client = Map.of(
                "username", request.email(),
                "email", request.email(),
                "firstName", request.firstName(),
                "lastName", request.lastName(),
                "enabled", true,
                "credentials", List.of(credentials)
        );

        HttpEntity<?> entity =
                new HttpEntity<>(client, headers);

        restTemplate.postForEntity(
                usersUrl(),
                entity,
                Void.class
        );

    }


    private void createDoctor(
            RegisterDoctorRequest request,
            String adminToken
    ) {

        var headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        var credentials = Map.of(
                "type", "password",
                "value", request.password(),
                "temporary", false
        );

        Map<String, Object> doctor = Map.of(
                "username", request.email(),
                "email", request.email(),
                "firstName", request.firstName(),
                "lastName", request.lastName(),
                "enabled", true,
                "credentials", List.of(credentials)
        );

        HttpEntity<?> entity =
                new HttpEntity<>(doctor, headers);

        restTemplate.postForEntity(
                usersUrl(),
                entity,
                Void.class
        );
    }

    private String getUserIdByUsername(
            String username,
            String adminToken
    ) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        HttpEntity<?> entity =
                new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response =
                restTemplate.exchange(
                        usersUrl() + "?username=" + username,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<>() {
                        }
                );

        return response.getBody()
                .getFirst()
                .get("id")
                .toString();
    }

    private RoleRepresentation getRealmRole(
            String roleName,
            String adminToken
    ) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        HttpEntity<Void> entity =
                new HttpEntity<>(headers);

        ResponseEntity<RoleRepresentation> response =
                restTemplate.exchange(
                        SERVER_URL
                                + "/admin/realms/"
                                + REALM
                                + "/roles/"
                                + roleName,
                        HttpMethod.GET,
                        entity,
                        RoleRepresentation.class
                );

        return response.getBody();
    }

    private void assignRole(
            String keycloakId,
            String roleName,
            String adminToken
    ) {
        RoleRepresentation role =
                getRealmRole(
                        roleName,
                        adminToken
                );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<RoleRepresentation>> entity =
                new HttpEntity<>(
                        List.of(role),
                        headers
                );

        restTemplate.postForEntity(
                realmRoleMappingsUrl(keycloakId),
                entity,
                Void.class
        );

    }

    private void removeRole(
            String keycloakId,
            String roleName,
            String adminToken
    ) {

        RoleRepresentation role =
                getRealmRole(
                        roleName,
                        adminToken
                );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<RoleRepresentation>> entity =
                new HttpEntity<>(
                        List.of(role),
                        headers
                );

        restTemplate.exchange(
                realmRoleMappingsUrl(keycloakId),
                HttpMethod.DELETE,
                entity,
                Void.class
        );
    }


    public void registerDoctorRequest(RegisterDoctorRequest request) {
        var adminToken = getAdminToken();

        createDoctor(request, adminToken);

        String keycloakId = getUserIdByUsername(request.email(), adminToken);

        assignRole(keycloakId, "dpasystem.DOCTOR_PENDING", adminToken); //KeyCloak role for realm

        doctorService.createDoctor(request, keycloakId);

    }

    public void registerClientRequest(RegisterClientRequest request) {
        var adminToken = getAdminToken();

        createClient(request, adminToken);

        String clientId = getUserIdByUsername(request.email(), adminToken);

        assignRole(clientId, "dpasystem.CLIENT", adminToken);

    }


    public AuthResponse login(String username, String password) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var body = new LinkedMultiValueMap<>();

        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", username);
        body.add("password", password);

        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        ResponseEntity<TokenResponse> response =
                restTemplate.postForEntity(
                        tokenUrl(),
                        entity,
                        TokenResponse.class
                );

        return new AuthResponse(
                Objects.requireNonNull(response.getBody()).accessToken()
        );
    }


}

