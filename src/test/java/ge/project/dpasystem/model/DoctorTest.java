package ge.project.dpasystem.model;

import org.junit.jupiter.api.Test;
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
        int experience = 10;
        int age = 45;

        Doctor doctor = new Doctor();
        doctor.id = id;
        doctor.firstName = firstName;
        doctor.lastName = lastName;
        doctor.specialization = specialization;
        doctor.experience = experience;
        doctor.age = age;

        assertEquals(id, doctor.id);
        assertEquals(firstName, doctor.firstName);
        assertEquals(lastName, doctor.lastName);
        assertEquals(specialization, doctor.specialization);
        assertEquals(experience, doctor.experience);
        assertEquals(age, doctor.age);
    }

    @Test
    void testAppointmentsAndReviews() {
        Doctor doctor = new Doctor();

        List<Appointment> appointments = List.of(new Appointment(), new Appointment());
        List<Review> reviews = List.of(new Review(), new Review());

        doctor.appointments = appointments;
        doctor.reviews = reviews;

        assertEquals(appointments, doctor.appointments);
        assertEquals(reviews, doctor.reviews);
    }
}
