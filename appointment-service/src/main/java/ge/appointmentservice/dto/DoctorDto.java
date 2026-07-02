package ge.appointmentservice.dto;


import ge.appointmentservice.model.Appointment;
import ge.appointmentservice.model.Review;
import ge.appointmentservice.model.Sex;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record DoctorDto(
        UUID id,
        String firstName,
        String lastName,
        Sex sex,
        String specialization,
        String professionalDescription,
        String phoneNumber,
        LocalDate dateOfBirth,
        String email,
        List<Appointment> appointments,
        List<Review> reviews,
        String adminComment,
        Double averageRating,
        Integer reviewsCount
) {

}
