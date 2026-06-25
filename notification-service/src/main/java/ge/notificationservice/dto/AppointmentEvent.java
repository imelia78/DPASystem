package ge.notificationservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentEvent(
        UUID appointmentId,
        String clientEmail,
        LocalDateTime appointmentDateTime,
        Integer appointmentDuration,
        String clientFirstName,
        String clientLastName,
        String doctorFirstName,
        String doctorLastName
) {
}
