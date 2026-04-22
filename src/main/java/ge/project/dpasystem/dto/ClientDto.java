package ge.project.dpasystem.dto;

import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.Review;
import ge.project.dpasystem.model.Sex;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Builder
public record ClientDto(
        String firstName,
        String lastName,
        String password,
        LocalDate dateOfBirth,
        Sex sex,
        List<Review> reviews,
        List<Appointment> appointments,
        String email,
        String phoneNumber
) {
}
