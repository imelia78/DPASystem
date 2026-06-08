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
import java.util.UUID;

@Builder
public record ClientDto(
    UUID id,
    String firstName,
    String lastName,
    String email,        // ← was password
    LocalDate dateOfBirth,
    Sex sex,
    List<Review> reviews,
    List<Appointment> appointments,
    String phoneNumber,
    String password      // ← move password here, or remove if unused in tests
) {
  public ClientDto() {
    this(null, null, null, null, null, null, null, null, null, null);
  }
}
