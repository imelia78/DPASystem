package ge.appointmentservice.dto;


import ge.appointmentservice.model.Appointment;
import ge.appointmentservice.model.Review;
import ge.appointmentservice.model.Sex;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record ClientDto(
        UUID id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        Sex sex,
        List<Review> reviews,
        List<Appointment> appointments,
        String email,
        String phoneNumber
) {
}
