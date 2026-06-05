package ge.project.dpasystem.dto.auth;

import ge.project.dpasystem.model.Sex;
import ge.project.dpasystem.model.VerificationStatus;

import java.time.LocalDate;

public record RegisterDoctorRequest (
        String firstName,
        String lastName,
        Sex sex,
        LocalDate dateOfBirth,
        String phoneNumber,
        String email,
        String password,
        String specialization,
        String professionalDescription,
        String stateCertificateNumber

) {
}
