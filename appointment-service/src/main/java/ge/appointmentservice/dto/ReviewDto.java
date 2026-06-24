package ge.appointmentservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ReviewDto(
        UUID id,
        String comment,
        Double rating,
        LocalDateTime createdAt,
        UUID appointmentId,
        UUID doctorId,
        UUID clientId
) {
}
