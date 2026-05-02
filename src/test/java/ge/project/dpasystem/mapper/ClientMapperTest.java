package ge.project.dpasystem.mapper;

import ge.project.dpasystem.dto.ClientDto;
import ge.project.dpasystem.model.Client;
import ge.project.dpasystem.model.Sex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    private ClientMapper clientMapper;

    @BeforeEach
    void setUp() {
        clientMapper = new ClientMapper();
    }

    @Test
    void testToDto() {
        Client client = new Client();
        ClientDto clientDto = clientMapper.toDto(client);

        assertEquals(client.getFirstName(), clientDto.firstName());
        assertEquals(client.getLastName(), clientDto.lastName());
        assertEquals(client.getEmail(), clientDto.email());
    }

    @Test
    void testToEntity() {
        ClientDto clientDto = new ClientDto(UUID.randomUUID(), "John", "Doe", "john.doe@example.com", LocalDate.now(), Sex.MALE, Collections.emptyList(), Collections.emptyList(), "1234567890", "Address");
        Client client = clientMapper.toEntity(clientDto);

        assertEquals(clientDto.firstName(), client.getFirstName());
        assertEquals(clientDto.lastName(), client.getLastName());
        assertEquals(clientDto.email(), client.getEmail());
    }

    @Test
    void testUpdateEntity() {
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setPhoneNumber("1234567890");

        ClientDto clientDto = ClientDto.builder()
                .firstName("Jane")
                .lastName("Smith")
                .phoneNumber("0987654321")
                .build();

        clientMapper.updateEntity(client, clientDto);

        assertEquals(clientDto.firstName(), client.getFirstName());
        assertEquals(clientDto.lastName(), client.getLastName());
        assertEquals(clientDto.phoneNumber(), client.getPhoneNumber());
    }
}
