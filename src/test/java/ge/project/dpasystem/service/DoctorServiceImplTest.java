package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.ClientDto;
import ge.project.dpasystem.dto.DoctorDto;
import ge.project.dpasystem.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllClientsByPages() {
        RequestFilter filter = mock(RequestFilter.class);
        when(filter.pageSize()).thenReturn(10);
        when(filter.pageNumber()).thenReturn(0);

        List<DoctorDto> result = doctorService.findAllClientsByPages(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();

        DoctorDto result = doctorService.findById(id);

        assertNull(result);
    }

    @Test
    void testCreateClient() {
        DoctorDto doctorDto = mock(DoctorDto.class);

        DoctorDto result = doctorService.createClient(doctorDto);

        assertNull(result);
    }

    @Test
    void testUpdateClient() {
        DoctorDto doctorDto = mock(DoctorDto.class);

        ClientDto result = doctorService.updateClient(doctorDto);

        assertNull(result);
    }

    @Test
    void testFindClientsByFirstNameAndLastName() {
        String firstName = "John";
        String lastName = "Doe";

        List<ClientDto> result = doctorService.findClientsByFirstNameAndLastName(firstName, lastName);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindClientByEmail() {
        String email = "test@example.com";

        ClientDto result = doctorService.findClientByEmail(email);

        assertNull(result);
    }

    @Test
    void testUpdateEmail() {
        UUID id = UUID.randomUUID();
        String email = "new@example.com";

        ClientDto result = doctorService.updateEmail(id, email);

        assertNull(result);
    }

    @Test
    void testUpdatePhoneNumber() {
        UUID id = UUID.randomUUID();
        String phoneNumber = "1234567890";

        ClientDto result = doctorService.updatePhoneNumber(id, phoneNumber);

        assertNull(result);
    }

    @Test
    void testDeleteByEmail() {
        String email = "test@example.com";

        doctorService.deleteByEmail(email);

        verifyNoInteractions(doctorRepository);
    }
}
