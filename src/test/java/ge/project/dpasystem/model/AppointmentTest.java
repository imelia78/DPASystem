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
        UUID id = UUID.randomUUID();
        appointment.setId(id);

        assertEquals(id, appointment.getId());
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
        Address address = new Address();

        Appointment appointment = Appointment.builder()
            .id(id)
            .appointmentDateTime(dateTime)
            .duration(duration)
            .price(price)
            .client(client)
            .doctor(doctor)
            .review(review)
            .appointmentAddress(address)
            .build();

        assertNotNull(appointment);
        assertEquals(id, appointment.getId());
        assertEquals(dateTime, appointment.getAppointmentDateTime());
        assertEquals(duration, appointment.getDuration());
        assertEquals(price, appointment.getPrice());
        assertEquals(client, appointment.getClient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(review, appointment.getReview());
        assertEquals(address, appointment.getAppointmentAddress());
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
        Address address = new Address();

        appointment.setId(id);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setDuration(duration);
        appointment.setPrice(price);
        appointment.setClient(client);
        appointment.setDoctor(doctor);
        appointment.setReview(review);
        appointment.setAppointmentAddress(address);

        assertEquals(id, appointment.getId());
        assertEquals(dateTime, appointment.getAppointmentDateTime());
        assertEquals(duration, appointment.getDuration());
        assertEquals(price, appointment.getPrice());
        assertEquals(client, appointment.getClient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(review, appointment.getReview());
        assertEquals(address, appointment.getAppointmentAddress());
    }
}
