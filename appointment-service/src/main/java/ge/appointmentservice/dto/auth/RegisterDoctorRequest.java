package ge.appointmentservice.dto.auth;



import ge.appointmentservice.model.Sex;

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
