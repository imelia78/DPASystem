package ge.appointmentservice.dto;

import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record UpdateAppointmentDateTime(
     @Future LocalDateTime dateTime,
        Integer duration

) {
}
