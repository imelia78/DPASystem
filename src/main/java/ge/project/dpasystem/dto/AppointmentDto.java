package ge.project.dpasystem.dto;

import ge.project.dpasystem.model.AppointmentStatus;
import ge.project.dpasystem.model.Client;
import ge.project.dpasystem.model.Doctor;
import ge.project.dpasystem.model.Review;
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
    String appointmentAddress,
    String description       // ← add this
) {}
