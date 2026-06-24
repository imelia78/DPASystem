package ge.appointmentservice.dto;

import ge.appointmentservice.model.*;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AppointmentDto(
    UUID id,
    LocalDateTime appointmentDateTime,
    Integer appointmentDuration,
    BigDecimal price,
    Client client,
    AppointmentStatus appointmentStatus,
    Doctor doctor,
    Review review,
    Address address
) {
}
