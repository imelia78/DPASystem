package ge.project.dpasystem.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RoleRepresentation(
        String id,
        String name
) {
}
