package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.AppointmentDto;
import ge.project.dpasystem.dto.AppointmentRequestDto;
import ge.project.dpasystem.dto.UpdateAppointmentDateTime;
import ge.project.dpasystem.dto.UpdateAppointmentStatus;
import ge.project.dpasystem.mapper.AppointmentMapper;
import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.AppointmentStatus;
import ge.project.dpasystem.model.Client;
import ge.project.dpasystem.model.Doctor;
import ge.project.dpasystem.repository.AppointmentRepository;
import ge.project.dpasystem.repository.ClientRepository;
import ge.project.dpasystem.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllAppointmentsByPages() {
        RequestFilter filter = mock(RequestFilter.class);
        when(filter.pageSize()).thenReturn(10);
        when(filter.pageNumber()).thenReturn(0);
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        Appointment appointment = new Appointment();
        AppointmentDto appointmentDto = mock(AppointmentDto.class);
        Page<Appointment> page = new PageImpl<>(List.of(appointment));
        when(appointmentRepository.findAllBy(pageable)).thenReturn(page);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        List<AppointmentDto> result = appointmentService.findAllAppointmentsByPages(filter);

        assertEquals(1, result.size());
        assertEquals(appointmentDto, result.getFirst());
    }

    @Test
    void testCreateAppointment() {
        AppointmentRequestDto appointmentRequest = mock(AppointmentRequestDto.class);

        UUID doctorId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();

        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(1);

        // --- DTO mocks ---
        var doctorDto = mock(ge.project.dpasystem.dto.DoctorDto.class);
        var clientDto = mock(ge.project.dpasystem.dto.ClientDto.class);

        when(doctorDto.id()).thenReturn(doctorId);
        when(clientDto.id()).thenReturn(clientId);

        when(appointmentRequest.doctor()).thenReturn(doctorDto);
        when(appointmentRequest.client()).thenReturn(clientDto);

        // IMPORTANT: fix null start issue
        when(appointmentRequest.dateTime()).thenReturn(appointmentTime);
        when(appointmentRequest.duration()).thenReturn(60);

        // --- domain objects ---
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        Client client = new Client();
        client.setId(clientId);

        Appointment appointment = new Appointment();
        AppointmentDto appointmentDto = mock(AppointmentDto.class);

        // --- repository stubs ---
        when(doctorRepository.findById(doctorId))
            .thenReturn(Optional.of(doctor));

        when(clientRepository.findClientById(clientId))
            .thenReturn(Optional.of(client));

        // IMPORTANT: avoid conflict logic breaking the test
        when(appointmentRepository.existsByDoctorIdAndAppointmentDateTimeBetween(
            any(), any(), any()))
            .thenReturn(false);

        when(appointmentRepository.existsByClientIdAndAppointmentDateTimeBetween(
            any(), any(), any()))
            .thenReturn(false);

        when(appointmentRepository.save(any(Appointment.class)))
            .thenReturn(appointment);

        when(appointmentMapper.toDto(appointment))
            .thenReturn(appointmentDto);

        // --- service call ---
        AppointmentDto result = appointmentService.createAppointment(appointmentRequest);

        // --- assertions ---
        assertNotNull(result);
        assertEquals(appointmentDto, result);
    }

    @Test
    void testUpdateAppointment() {
        AppointmentDto appointmentDto = mock(AppointmentDto.class);
        Appointment appointment = new Appointment();
        UUID id = UUID.randomUUID();
        when(appointmentDto.id()).thenReturn(id);
        when(appointmentRepository.findAppointmentById(id)).thenReturn(Optional.of(appointment));

        AppointmentDto updatedDto = mock(AppointmentDto.class);
        when(appointmentMapper.toDto(appointment)).thenReturn(updatedDto);

        AppointmentDto result = appointmentService.updateAppointment(appointmentDto);

        assertEquals(updatedDto, result);
        verify(appointmentMapper).updateAppointment(appointment, appointmentDto);
    }

    @Test
    void testUpdateAppointmentStatus() {
        UUID id = UUID.randomUUID();
        UpdateAppointmentStatus request = mock(UpdateAppointmentStatus.class);
        Appointment appointment = new Appointment();
        when(appointmentRepository.findAppointmentById(id)).thenReturn(Optional.of(appointment));
        when(request.updatedStatus()).thenReturn(AppointmentStatus.CONFIRMED);

        AppointmentDto appointmentDto = mock(AppointmentDto.class);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        AppointmentDto result = appointmentService.updateAppointmentStatus(id, request);

        assertEquals(appointmentDto, result);
        assertEquals(AppointmentStatus.CONFIRMED, appointment.getAppointmentStatus());
    }

    @Test
    void testUpdateAppointmentDateOrTime() {
        UUID id = UUID.randomUUID();
        UpdateAppointmentDateTime request = mock(UpdateAppointmentDateTime.class);
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        when(request.dateTime()).thenReturn(futureDate);
        when(request.duration()).thenReturn(60);

        Appointment appointment = new Appointment();
        Doctor doctor = new Doctor();
        doctor.setId(UUID.randomUUID());
        Client client = new Client();
        client.setId(UUID.randomUUID());
        appointment.setDoctor(doctor);
        appointment.setClient(client);

        when(appointmentRepository.findAppointmentById(id)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.existsByDoctorIdAndAppointmentDateTimeBetween(any(), any(), any())).thenReturn(false);
        when(appointmentRepository.existsByClientIdAndAppointmentDateTimeBetween(any(), any(), any())).thenReturn(false);

        AppointmentDto appointmentDto = mock(AppointmentDto.class);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        AppointmentDto result = appointmentService.updateAppointmentDateOrTime(id, request);

        assertEquals(appointmentDto, result);
        assertEquals(futureDate, appointment.getAppointmentDateTime());
    }

    @Test
    void testFindAppointmentsByDateRange() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        Appointment appointment = new Appointment();
        AppointmentDto appointmentDto = mock(AppointmentDto.class);
        when(appointmentRepository.findAppointmentsByAppointmentDateTimeBetween(start, end)).thenReturn(List.of(appointment));
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        List<AppointmentDto> result = appointmentService.findAppointmentsByDateRange(start, end);

        assertEquals(1, result.size());
        assertEquals(appointmentDto, result.getFirst());
    }
}
