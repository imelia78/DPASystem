package ge.project.dpasystem.dto;

import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.Review;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record DoctorDto(
        UUID id,
        String firstName,
        String lastName,
        String specialization,
        Integer experience,
        Integer age,
        String email,
        List<Appointment> appointments,
        List<Review> reviews
) {
}
