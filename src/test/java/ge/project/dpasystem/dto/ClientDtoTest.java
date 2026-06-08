package ge.project.dpasystem.dto;

import ge.project.dpasystem.model.Sex;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientDtoTest {

    @Test
    void testClientDto() {
        ClientDto clientDto = new ClientDto(UUID.randomUUID(), "John", "Doe", "john.doe@example.com", LocalDate.now(), Sex.MALE, Collections.emptyList(), Collections.emptyList(), "1234567890", "Address");

        assertEquals("John", clientDto.firstName());
        assertEquals("Doe", clientDto.lastName());
        assertEquals("john.doe@example.com", clientDto.email());
    }
}
