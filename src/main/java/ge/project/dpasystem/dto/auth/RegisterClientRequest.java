package ge.project.dpasystem.dto.auth;

import ge.project.dpasystem.model.Sex;

import java.time.LocalDate;

public record RegisterClientRequest(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String email,
        String phoneNumber,
        Sex sex,
        String password
) {
}
