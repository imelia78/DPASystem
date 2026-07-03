package ge.appointmentservice.service;


import ge.appointmentservice.dto.auth.AuthResponse;
import ge.appointmentservice.dto.auth.RoleRepresentation;
import ge.appointmentservice.dto.auth.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakAdminServiceImpl implements KeycloakAdminService {

    private final RestTemplate restTemplate;


    private static final String SERVER_URL = "http://keycloak:8080";

    private static final String REALM = "dpasystem";

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


    @Override
    public String createUser(String email, String firstName, String lastName, String password) {

        var headers = new HttpHeaders();

        String adminToken = getAdminToken();

        headers.setBearerAuth(adminToken);

        var credentials = Map.of(
                "type", "password",
                "value", password,
                "temporary", false
        );

        var user = Map.of(
                "username", email,
                "email", email,
                "firstName", firstName,
                "lastName", lastName,
                "enabled", true,
                "credentials", List.of(credentials)
        );

        HttpEntity<?> entity =
                new HttpEntity<>(user, headers);

        ResponseEntity<Void> response =
                restTemplate.postForEntity(
                        usersUrl(),
                        entity,
                        Void.class
                );

        URI location = Objects.requireNonNull(
                response.getHeaders().getLocation());

        String path = location.getPath();

        return path.substring(path.lastIndexOf('/') + 1);
    }

    @Override
    public void deleteUser(String keycloakId) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(getAdminToken());

        var entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                usersUrl() + "/" + keycloakId, HttpMethod.DELETE, entity, Void.class
        );

        log.info(
                "User {} deleted from Keycloak",
                keycloakId
        );
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

    @Override
    public void assignRole(String keycloakId, String roleName) {

        String adminToken = getAdminToken();

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

    @Override
    public void removeRole(String keycloakId, String roleName) {

        String adminToken = getAdminToken();

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


    public AuthResponse login(String username, String password) {

        try {
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
            TokenResponse responseBody = Objects.requireNonNull(response.getBody());


            return new AuthResponse(
                    responseBody.accessToken(),
                    responseBody.refreshToken(),
                    responseBody.expiresIn(),
                    responseBody.tokenType()
            );
        } catch (HttpClientErrorException.Unauthorized e) {
            log.warn("Failed authentication attempt for user {}", username);

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password!"
            );

        } catch (RestClientException e) {

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Authentication service unavailable!"
            );

        }


    }

    @Override
    public void updateUserEmail(String keycloakId, String newEmail) {
        String adminToken = getAdminToken();


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("email", newEmail);
        body.put("username", newEmail);
        body.put("emailVerified", false);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        restTemplate.exchange(
                SERVER_URL +
                        "/admin/realms/" +
                        REALM +
                        "/users/" +
                        keycloakId,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        log.info("Email updated in Keycloak for user {}", keycloakId);

    }


}
