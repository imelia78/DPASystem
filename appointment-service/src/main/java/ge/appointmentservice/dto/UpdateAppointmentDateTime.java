package ge.appointmentservice.dto;

import java.time.LocalDateTime;

public record UpdateAppointmentDateTime(
        LocalDateTime dateTime,
        Integer duration

) {
}
