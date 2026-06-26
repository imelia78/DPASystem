package ge.appointmentservice.dto;



import ge.appointmentservice.model.Address;

import java.time.LocalDateTime;
import java.util.UUID;




public record AppointmentRequestDto(
        UUID clientId,
        UUID doctorId,
        LocalDateTime dateTime,
        Address address,
        Integer duration
) {
}
