package ge.project.dpasystem.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    @Test
    void testReviewFields() {
        UUID id = UUID.randomUUID();
        double rating = 4.5;
        String comment = "Excellent service!";
        Appointment appointment = new Appointment();
        Doctor doctor = new Doctor();
        Client client = new Client();
        LocalDateTime createdAt = LocalDateTime.now();

        Review review = new Review();
        review.setId(id);
        review.setRating(rating);
        review.setComment(comment);
        review.setAppointment(appointment);
        review.setDoctor(doctor);
        review.setClient(client);
        review.setCreatedAt(createdAt);

        assertEquals(id, review.getId());
        assertEquals(rating, review.getRating());
        assertEquals(comment, review.getComment());
        assertEquals(appointment, review.getAppointment());
        assertEquals(doctor, review.getDoctor());
        assertEquals(client, review.getClient());
        assertEquals(createdAt, review.getCreatedAt());
    }

    @Test
    void testOnCreate() {
        Review review = new Review();
        review.onCreate();

        assertNotNull(review.getCreatedAt());
        assertTrue(review.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
