package ge.project.dpasystem.controller;

import ge.project.dpasystem.dto.ClientDto;
import ge.project.dpasystem.dto.UpdateEmailDto;
import ge.project.dpasystem.dto.UpdatePhoneDto;
import ge.project.dpasystem.service.ClientService;
import ge.project.dpasystem.model.Sex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClients() {
        Integer pageSize = 10;
        Integer pageNumber = 0;
        RequestFilter filter = new RequestFilter(pageSize, pageNumber);
        List<ClientDto> clientDtos = List.of(new ClientDto(UUID.randomUUID(), "John", "Doe", "john.doe@example.com", LocalDate.now(), Sex.MALE, Collections.emptyList(), Collections.emptyList(), "1234567890", "Address"));
        when(clientService.findAllClientsByPages(filter)).thenReturn(clientDtos);

        List<ClientDto> result = clientController.getAllClients(pageSize, pageNumber).getBody();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clientService, times(1)).findAllClientsByPages(filter);
    }

    @Test
    void testGetClientByEmail() {
        String email = "test@example.com";
        ClientDto clientDto = new ClientDto();
        when(clientService.findClientByEmail(email)).thenReturn(clientDto);

        ResponseEntity<ClientDto> response = clientController.getClientByEmail(email);

        assertNotNull(response.getBody());
        assertEquals(clientDto, response.getBody());
        verify(clientService, times(1)).findClientByEmail(email);
    }

    @Test
    void testGetClientById() {
        UUID id = UUID.randomUUID();
        ClientDto clientDto = new ClientDto();
        when(clientService.findClientById(id)).thenReturn(clientDto);

        ResponseEntity<ClientDto> response = clientController.getClientById(id);

        assertNotNull(response.getBody());
        assertEquals(clientDto, response.getBody());
        verify(clientService, times(1)).findClientById(id);
    }

    @Test
    void testCreateClient() {
        ClientDto clientDto = new ClientDto();
        when(clientService.createClient(clientDto)).thenReturn(clientDto);

        ResponseEntity<ClientDto> response = clientController.createClient(clientDto);

        assertNotNull(response.getBody());
        assertEquals(clientDto, response.getBody());
        verify(clientService, times(1)).createClient(clientDto);
    }

    @Test
    void testUpdateClient() {
        ClientDto clientDto = new ClientDto();
        when(clientService.updateClient(clientDto)).thenReturn(clientDto);

        ResponseEntity<ClientDto> response = clientController.updateClient(clientDto);

        assertNotNull(response.getBody());
        assertEquals(clientDto, response.getBody());
        verify(clientService, times(1)).updateClient(clientDto);
    }

    @Test
    void testUpdateClientPhoneNumber() {
        UUID id = UUID.randomUUID();
        UpdatePhoneDto phoneDto = new UpdatePhoneDto("1234567890");
        ClientDto clientDto = new ClientDto();
        when(clientService.updatePhoneNumber(id, phoneDto.phoneNumber())).thenReturn(clientDto);

        ResponseEntity<ClientDto> response = clientController.updateClientPhoneNumber(id, phoneDto);

        assertNotNull(response.getBody());
        assertEquals(clientDto, response.getBody());
        verify(clientService, times(1)).updatePhoneNumber(id, phoneDto.phoneNumber());
    }

    @Test
    void testUpdateClientEmail() {
        UUID id = UUID.randomUUID();
        UpdateEmailDto emailDto = new UpdateEmailDto("newemail@example.com");
        ClientDto clientDto = new ClientDto();
        when(clientService.updateEmail(id, emailDto.email())).thenReturn(clientDto);

        ResponseEntity<ClientDto> response = clientController.updateClientEmail(id, emailDto);

        assertNotNull(response.getBody());
        assertEquals(clientDto, response.getBody());
        verify(clientService, times(1)).updateEmail(id, emailDto.email());
    }
}
