package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.ClientDto;
import ge.project.dpasystem.mapper.ClientMapper;
import ge.project.dpasystem.model.Client;
import ge.project.dpasystem.model.Sex;
import ge.project.dpasystem.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClientServiceImpl(clientRepository, clientMapper);
    }

    @Test
    void testFindAllClientsByPages() {
        RequestFilter filter = mock(RequestFilter.class);
        when(filter.pageSize()).thenReturn(10);
        when(filter.pageNumber()).thenReturn(0);

        Pageable pageable = Pageable.ofSize(10).withPage(0);

        Client client = new Client();
        Page<Client> page = new PageImpl<>(Collections.singletonList(client));

        when(clientRepository.findAllBy(pageable)).thenReturn(page);

        ClientDto clientDto = new ClientDto(
            UUID.randomUUID(),
            "John",
            "Doe",
            "john.doe@example.com",
            LocalDate.now(),
            Sex.MALE,
            Collections.emptyList(),
            Collections.emptyList(),
            "1234567890",
            "Address"
        );

        when(clientMapper.toDto(client)).thenReturn(clientDto);

        List<ClientDto> result = clientService.findAllClientsByPages(filter);

        assertEquals(1, result.size());
        verify(clientRepository).findAllBy(pageable);
        verify(clientMapper).toDto(client);
    }

    @Test
    void testFindClientById() {
        UUID uuid = UUID.randomUUID();
        Client client = new Client();

        when(clientRepository.findClientById(uuid)).thenReturn(Optional.of(client));

        ClientDto clientDto = new ClientDto();
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        ClientDto result = clientService.findClientById(uuid);

        assertNotNull(result);
        verify(clientRepository).findClientById(uuid);
        verify(clientMapper).toDto(client);
    }

    @Test
    void testCreateClient() {
        ClientDto clientDto = new ClientDto();
        Client client = new Client();

        when(clientMapper.toEntity(clientDto)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        ClientDto result = clientService.createClient(clientDto);

        assertNotNull(result);
        verify(clientMapper).toEntity(clientDto);
        verify(clientRepository).save(client);
        verify(clientMapper).toDto(client);
    }

    @Test
    void testFindClientByEmail() {
        String email = "test@example.com";
        Client client = new Client();

        when(clientRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(client));

        ClientDto clientDto = new ClientDto();
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        ClientDto result = clientService.findClientByEmail(email);

        assertNotNull(result);
        verify(clientRepository).findByEmail(anyString());
        verify(clientMapper).toDto(client);
    }

    @Test
    void testDeleteClientByEmail() {
        String email = "test@example.com";
        Client client = new Client();

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));

        clientService.deleteClientByEmail(email);

        verify(clientRepository).findByEmail(eq(email));
        verify(clientRepository).deleteByEmail(eq(email));
    }

    @Test
    void testUpdateEmail() {
        UUID id = UUID.randomUUID();
        String newEmail = "new@example.com";
        Client client = new Client();

        when(clientRepository.findClientById(id)).thenReturn(Optional.of(client));
        when(clientRepository.existsByEmail(newEmail)).thenReturn(false);

        ClientDto clientDto = new ClientDto();
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        ClientDto result = clientService.updateEmail(id, newEmail);

        assertEquals(clientDto, result);
        assertEquals(newEmail, client.getEmail());
    }

    @Test
    void testUpdatePhoneNumber() {
        UUID id = UUID.randomUUID();
        String newPhoneNumber = "1234567890";
        Client client = new Client();

        when(clientRepository.findClientById(id)).thenReturn(Optional.of(client));
        when(clientRepository.existsByPhoneNumber(newPhoneNumber)).thenReturn(false);

        ClientDto clientDto = new ClientDto();
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        ClientDto result = clientService.updatePhoneNumber(id, newPhoneNumber);

        assertEquals(clientDto, result);
        assertEquals(newPhoneNumber, client.getPhoneNumber());
    }
}