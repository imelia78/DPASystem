package ge.appointmentservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UpdateEmailDto(
 @NotNull @Email String email
) {
}
