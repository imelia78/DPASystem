package ge.project.dpasystem.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    @Test
    void testAppointmentModel() {
        Appointment appointment = new Appointment();
        appointment.setDescription("Test Appointment");

        assertEquals("Test Appointment", appointment.getDescription());
    }

    @Test
    void testAppointmentBuilder() {
        UUID id = UUID.randomUUID();
        LocalDateTime dateTime = LocalDateTime.now();
        Integer duration = 30;
        BigDecimal price = BigDecimal.valueOf(100);
        Client client = new Client();
        Doctor doctor = new Doctor();
        Review review = new Review();
        String address = "123 Main St";
        String description = "General check-up";

        Appointment appointment = Appointment.builder()
                .id(id)
                .appointmentDateTime(dateTime)
                .appointmentDuration(duration)
                .price(price)
                .client(client)
                .doctor(doctor)
                .review(review)
                .appointmentAddress(address)
                .description(description)
                .build();

        assertNotNull(appointment);
        assertEquals(id, appointment.getId());
        assertEquals(dateTime, appointment.getAppointmentDateTime());
        assertEquals(duration, appointment.getAppointmentDuration());
        assertEquals(price, appointment.getPrice());
        assertEquals(client, appointment.getClient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(review, appointment.getReview());
        assertEquals(address, appointment.getAppointmentAddress());
        assertEquals(description, appointment.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        Appointment appointment = new Appointment();
        UUID id = UUID.randomUUID();
        LocalDateTime dateTime = LocalDateTime.now();
        Integer duration = 45;
        BigDecimal price = BigDecimal.valueOf(150);
        Client client = new Client();
        Doctor doctor = new Doctor();
        Review review = new Review();
        String address = "456 Elm St";
        String description = "Follow-up visit";

        appointment.setId(id);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setAppointmentDuration(duration);
        appointment.setPrice(price);
        appointment.setClient(client);
        appointment.setDoctor(doctor);
        appointment.setReview(review);
        appointment.setAppointmentAddress(address);
        appointment.setDescription(description);

        assertEquals(id, appointment.getId());
        assertEquals(dateTime, appointment.getAppointmentDateTime());
        assertEquals(duration, appointment.getAppointmentDuration());
        assertEquals(price, appointment.getPrice());
        assertEquals(client, appointment.getClient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(review, appointment.getReview());
        assertEquals(address, appointment.getAppointmentAddress());
        assertEquals(description, appointment.getDescription());
    }
}
