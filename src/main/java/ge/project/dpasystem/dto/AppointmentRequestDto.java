package ge.project.dpasystem.dto;

import ge.project.dpasystem.model.Address;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequestDto(
        UUID id,
        LocalDateTime dateTime,
        DoctorDto doctor,
        ClientDto client,
        Address address,
        Integer duration
) {
}
