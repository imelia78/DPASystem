package ge.project.dpasystem.controller;

import ge.project.dpasystem.dto.AppointmentDto;
import ge.project.dpasystem.dto.AppointmentRequestDto;
import ge.project.dpasystem.dto.UpdateAppointmentDateTime;
import ge.project.dpasystem.dto.UpdateAppointmentStatus;
import ge.project.dpasystem.model.AppointmentStatus;
import ge.project.dpasystem.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAppointments() {
        var filter = new RequestFilter(10, 0);
        List<AppointmentDto> appointments = Collections.emptyList();
        when(appointmentService.findAllAppointmentsByPages(filter)).thenReturn(appointments);

        ResponseEntity<List<AppointmentDto>> response = appointmentController.getAllAppointments(10, 0);

        assertEquals(appointments, response.getBody());
        verify(appointmentService, times(1)).findAllAppointmentsByPages(filter);
    }

    @Test
    void testGetAppointmentsByClient() {
        UUID clientId = UUID.randomUUID();
        var filter = new RequestFilter(10, 0);
        List<AppointmentDto> appointments = Collections.emptyList();
        when(appointmentService.findAllAppointmentsByClientId(clientId, filter)).thenReturn(appointments);

        ResponseEntity<List<AppointmentDto>> response = appointmentController.getAppointmentsByClient(clientId, 10, 0);

        assertEquals(appointments, response.getBody());
        verify(appointmentService, times(1)).findAllAppointmentsByClientId(clientId, filter);
    }

    @Test
    void testGetAppointmentByStatus() {
        AppointmentStatus status = AppointmentStatus.CONFIRMED;
        List<AppointmentDto> appointments = Collections.emptyList();
        when(appointmentService.findAppointmentsByStatus(status)).thenReturn(appointments);

        ResponseEntity<List<AppointmentDto>> response = appointmentController.getAppointmentByStatus(status);

        assertEquals(appointments, response.getBody());
        verify(appointmentService, times(1)).findAppointmentsByStatus(status);
    }

    @Test
    void testGetAppointmentsByDateRange() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<AppointmentDto> appointments = Collections.emptyList();
        when(appointmentService.findAppointmentsByDateRange(start, end)).thenReturn(appointments);

        ResponseEntity<List<AppointmentDto>> response = appointmentController.getAppointmentsByDateRange(start, end);

        assertEquals(appointments, response.getBody());
        verify(appointmentService, times(1)).findAppointmentsByDateRange(start, end);
    }

    @Test
    void testCreateAppointment() {
        // Use mock — no need to construct the record with all fields in a controller test
        AppointmentRequestDto appointmentRequest = mock(AppointmentRequestDto.class);
        AppointmentDto appointmentDto = mock(AppointmentDto.class);
        when(appointmentService.createAppointment(appointmentRequest)).thenReturn(appointmentDto);

        ResponseEntity<AppointmentDto> response = appointmentController.createAppointment(appointmentRequest);

        assertEquals(appointmentDto, response.getBody());
        verify(appointmentService, times(1)).createAppointment(appointmentRequest);
    }

    @Test
    void testUpdateAppointmentStatus() {
        UUID appointmentId = UUID.randomUUID();
        UpdateAppointmentStatus request = new UpdateAppointmentStatus(AppointmentStatus.CONFIRMED);
        AppointmentDto appointmentDto = mock(AppointmentDto.class);
        when(appointmentService.updateAppointmentStatus(appointmentId, request)).thenReturn(appointmentDto);

        ResponseEntity<AppointmentDto> response = appointmentController.updateAppointmentStatus(appointmentId, request);

        assertEquals(appointmentDto, response.getBody());
        verify(appointmentService, times(1)).updateAppointmentStatus(appointmentId, request);
    }

    @Test
    void testUpdateAppointmentDateOrTime() {
        UUID appointmentId = UUID.randomUUID();
        UpdateAppointmentDateTime request = new UpdateAppointmentDateTime(LocalDateTime.now().plusDays(1), 60);
        AppointmentDto appointmentDto = mock(AppointmentDto.class);
        when(appointmentService.updateAppointmentDateOrTime(appointmentId, request)).thenReturn(appointmentDto);

        ResponseEntity<AppointmentDto> response = appointmentController.updateAppointmentDateOrTime(appointmentId, request);

        assertEquals(appointmentDto, response.getBody());
        verify(appointmentService, times(1)).updateAppointmentDateOrTime(appointmentId, request);
    }

    @Test
    void testUpdateAppointment() {
        UUID appointmentId = UUID.randomUUID();
        AppointmentDto appointmentDto = mock(AppointmentDto.class);
        when(appointmentService.updateAppointment(appointmentDto)).thenReturn(appointmentDto);

        ResponseEntity<AppointmentDto> response = appointmentController.updateAppointment(appointmentId, appointmentDto);

        assertEquals(appointmentDto, response.getBody());
        verify(appointmentService, times(1)).updateAppointment(appointmentDto);
    }
}