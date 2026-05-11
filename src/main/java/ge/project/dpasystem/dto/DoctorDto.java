package ge.project.dpasystem.dto;

import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.Review;
import ge.project.dpasystem.model.Sex;
import jakarta.persistence.criteria.CriteriaBuilder;
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
        List<Review> reviews
) {
}
