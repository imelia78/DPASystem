package ge.project.dpasystem.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {

    @Test
    void testDoctorFields() {
        UUID id = UUID.randomUUID();
        String firstName = "John";
        String lastName = "Doe";
        String specialization = "Cardiology";
        String professionalDescription = "Experienced cardiologist";
        LocalDate dateOfBirth = LocalDate.of(1975, 5, 15);
        String phoneNumber = "1234567890";
        String email = "john@example.com";

        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setSpecialization(specialization);
        doctor.setProfessionalDescription(professionalDescription);
        doctor.setDateOfBirth(dateOfBirth);
        doctor.setPhoneNumber(phoneNumber);
        doctor.setEmail(email);

        assertEquals(id, doctor.getId());
        assertEquals(firstName, doctor.getFirstName());
        assertEquals(lastName, doctor.getLastName());
        assertEquals(specialization, doctor.getSpecialization());
        assertEquals(professionalDescription, doctor.getProfessionalDescription());
        assertEquals(dateOfBirth, doctor.getDateOfBirth());
        assertEquals(phoneNumber, doctor.getPhoneNumber());
        assertEquals(email, doctor.getEmail());
    }

    @Test
    void testAppointmentsAndReviews() {
        Doctor doctor = new Doctor();

        List<Appointment> appointments = List.of(new Appointment(), new Appointment());
        List<Review> reviews = List.of(new Review(), new Review());

        doctor.setAppointments(appointments);
        doctor.setReviews(reviews);

        assertEquals(appointments, doctor.getAppointments());
        assertEquals(reviews, doctor.getReviews());
    }
}
