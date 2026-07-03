package ge.appointmentservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfessionalDescriptionDto(
    @NotBlank String professionalDescription
) {
}
