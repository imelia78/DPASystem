package ge.project.dpasystem.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void testClientModel() {
        Client client = Client.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();

        assertEquals("John", client.getFirstName());
        assertEquals("Doe", client.getLastName());
        assertEquals("john.doe@example.com", client.getEmail());
    }

    @Test
    void testComputeAge() {
        Client client = Client.builder()
            .dateOfBirth(LocalDate.of(1990, 5, 1))
            .build();

        int age = client.computeAge();

        assertEquals(36, age); // Assuming the current year is 2026
    }

    @Test
    void testRetireeVerificationMale() {
        Client client = Client.builder()
            .dateOfBirth(LocalDate.of(1950, 1, 1))
            .sex(Sex.MALE)
            .build();

        assertTrue(client.retireeVerification());
    }

    @Test
    void testRetireeVerificationFemale() {
        Client client = Client.builder()
            .dateOfBirth(LocalDate.of(1960, 1, 1))
            .sex(Sex.FEMALE)
            .build();

        assertTrue(client.retireeVerification());
    }

    @Test
    void testRetireeVerificationNotRetired() {
        Client client = Client.builder()
            .dateOfBirth(LocalDate.of(2000, 1, 1))
            .sex(Sex.MALE)
            .build();

        assertFalse(client.retireeVerification());
    }

    @Test
    void testClientBuilder() {
        UUID id = UUID.randomUUID();
        Client client = Client.builder()
            .id(id)
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .password("password123")
            .phoneNumber("1234567890")
            .dateOfBirth(LocalDate.of(1990, 5, 1))
            .sex(Sex.MALE)
            .build();

        assertNotNull(client);
        assertEquals(id, client.getId());
        assertEquals("John", client.getFirstName());
        assertEquals("Doe", client.getLastName());
        assertEquals("john.doe@example.com", client.getEmail());
        assertEquals("password123", client.getPassword());
        assertEquals("1234567890", client.getPhoneNumber());
        assertEquals(LocalDate.of(1990, 5, 1), client.getDateOfBirth());
        assertEquals(Sex.MALE, client.getSex());
    }
}
