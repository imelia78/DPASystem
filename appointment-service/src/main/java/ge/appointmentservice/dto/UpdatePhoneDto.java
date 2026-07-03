package ge.appointmentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdatePhoneDto(
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+995\\d{9}$", message = "Phone number must be in format +995XXXXXXXXX")
        String phoneNumber
) {
}
