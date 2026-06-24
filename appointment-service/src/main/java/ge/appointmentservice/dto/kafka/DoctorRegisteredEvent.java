package ge.appointmentservice.dto.kafka;

import java.util.UUID;

public record DoctorRegisteredEvent(
        UUID doctorId,
        String email,
        String firstName,
        String lastName
) {
}
