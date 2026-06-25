package ge.appointmentservice.dto.kafka;

import ge.appointmentservice.model.AppointmentStatus;
import ge.appointmentservice.model.Client;
import ge.appointmentservice.model.Doctor;

import java.math.BigDecimal;
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
