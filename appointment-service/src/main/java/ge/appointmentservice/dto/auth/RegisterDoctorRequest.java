package ge.appointmentservice.dto.auth;


import ge.appointmentservice.model.Sex;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterDoctorRequest(
        @NotBlank @Size(max = 70) String firstName,
        @NotBlank @Size(max = 70) String lastName,
        @NotNull Sex sex,
        @NotNull @Past LocalDate dateOfBirth,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+995\\d{9}$", message = "Phone number must be in format +995XXXXXXXXX")
        String phoneNumber,

        @NotBlank @Email String email,
        @Size(min = 8, max = 72) String password,
        @NotNull String specialization,
        @NotBlank String professionalDescription,
        @NotBlank String stateCertificateNumber

) {
}
