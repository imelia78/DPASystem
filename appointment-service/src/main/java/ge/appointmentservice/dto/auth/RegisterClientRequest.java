package ge.appointmentservice.dto.auth;



import ge.appointmentservice.model.Sex;

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
