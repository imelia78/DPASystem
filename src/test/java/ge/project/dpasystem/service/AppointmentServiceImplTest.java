package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.AppointmentDto;
import ge.project.dpasystem.dto.UpdateAppointmentDateTime;
import ge.project.dpasystem.dto.UpdateAppointmentStatus;
import ge.project.dpasystem.mapper.AppointmentMapper;
import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.AppointmentStatus;
import ge.project.dpasystem.repository.AppointmentRepository;
import jakarta.persistence.EntityNotFoundException;
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
        assertEquals(appointmentDto, result.get(0));
    }

    @Test
    void testCreateAppointment() {
        AppointmentDto appointmentDto = mock(AppointmentDto.class);
        Appointment appointment = new Appointment();
        when(appointmentMapper.toAppointment(appointmentDto)).thenReturn(appointment);
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        AppointmentDto result = appointmentService.createAppointment(appointmentDto);

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

        Appointment appointment = new Appointment();
        when(appointmentRepository.findAppointmentById(id)).thenReturn(Optional.of(appointment));

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
        assertEquals(appointmentDto, result.get(0));
    }
}
