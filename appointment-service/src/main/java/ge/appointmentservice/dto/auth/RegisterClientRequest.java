package ge.appointmentservice.dto.auth;


import ge.appointmentservice.model.Sex;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterClientRequest(
        @NotBlank @Size(max = 70) String firstName,
        @NotBlank @Size(max = 70) String lastName,
        @NotNull @Past  LocalDate dateOfBirth,
        @NotNull @Email String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+995\\d{9}$", message = "Phone number must be in format +995XXXXXXXXX")
        String phoneNumber,

        @NotNull  Sex sex,
        @Size(min = 8, max = 72) String password
) {
}
