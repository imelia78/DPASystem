package ge.project.dpasystem.mapper;

import ge.project.dpasystem.dto.AppointmentDto;
import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.AppointmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentMapperTest {

    private AppointmentMapper appointmentMapper;

    @BeforeEach
    void setUp() {
        appointmentMapper = new AppointmentMapper();
    }

    @Test
    void testToDto() {
        Appointment appointment = Appointment.builder()
                .id(UUID.randomUUID())
                .appointmentDateTime(LocalDateTime.now())
                .appointmentDuration(30)
                .price(BigDecimal.valueOf(100))
                .appointmentStatus(AppointmentStatus.CONFIRMED)
                .appointmentAddress("123 Main St")
                .build();

        AppointmentDto appointmentDto = appointmentMapper.toDto(appointment);

        assertNotNull(appointmentDto);
        assertEquals(appointment.getId(), appointmentDto.id());
        assertEquals(appointment.getAppointmentDateTime(), appointmentDto.appointmentDateTime());
        assertEquals(appointment.getAppointmentDuration(), appointmentDto.appointmentDuration());
        assertEquals(appointment.getPrice(), appointmentDto.price());
        assertEquals(appointment.getAppointmentStatus(), appointmentDto.appointmentStatus());
        assertEquals(appointment.getAppointmentAddress(), appointmentDto.appointmentAddress());
    }

    @Test
    void testToAppointment() {
        AppointmentDto appointmentDto = AppointmentDto.builder()
                .id(UUID.randomUUID())
                .appointmentDateTime(LocalDateTime.now())
                .appointmentDuration(30)
                .price(BigDecimal.valueOf(100))
                .appointmentStatus(AppointmentStatus.CONFIRMED)
                .appointmentAddress("123 Main St")
                .build();

        Appointment appointment = appointmentMapper.toAppointment(appointmentDto);

        assertNotNull(appointment);
        assertEquals(appointmentDto.id(), appointment.getId());
        assertEquals(appointmentDto.appointmentDateTime(), appointment.getAppointmentDateTime());
        assertEquals(appointmentDto.appointmentDuration(), appointment.getAppointmentDuration());
        assertEquals(appointmentDto.price(), appointment.getPrice());
        assertEquals(appointmentDto.appointmentStatus(), appointment.getAppointmentStatus());
        assertEquals(appointmentDto.appointmentAddress(), appointment.getAppointmentAddress());
    }

    @Test
    void testUpdateAppointment() {
        Appointment appointment = Appointment.builder()
                .id(UUID.randomUUID())
                .appointmentDateTime(LocalDateTime.now())
                .appointmentDuration(30)
                .price(BigDecimal.valueOf(100))
                .appointmentStatus(AppointmentStatus.CONFIRMED)
                .appointmentAddress("123 Main St")
                .build();

        AppointmentDto appointmentDto = AppointmentDto.builder()
                .id(UUID.randomUUID())
                .appointmentDateTime(LocalDateTime.now().plusDays(1))
                .appointmentDuration(60)
                .price(BigDecimal.valueOf(200))
                .appointmentStatus(AppointmentStatus.CANCELLED)
                .appointmentAddress("456 Elm St")
                .build();

        appointmentMapper.updateAppointment(appointment, appointmentDto);

        assertEquals(appointmentDto.id(), appointment.getId());
        assertEquals(appointmentDto.appointmentDateTime(), appointment.getAppointmentDateTime());
        assertEquals(appointmentDto.appointmentDuration(), appointment.getAppointmentDuration());
        assertEquals(appointmentDto.price(), appointment.getPrice());
        assertEquals(appointmentDto.appointmentStatus(), appointment.getAppointmentStatus());
        assertEquals(appointmentDto.appointmentAddress(), appointment.getAppointmentAddress());
    }
}
