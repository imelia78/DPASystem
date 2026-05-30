package ge.project.dpasystem.mapper;

import ge.project.dpasystem.dto.AppointmentDto;
import ge.project.dpasystem.model.Address;
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
        Address address = new Address();
        Appointment appointment = Appointment.builder()
            .id(UUID.randomUUID())
            .appointmentDateTime(LocalDateTime.now())
            .duration(30)
            .price(BigDecimal.valueOf(100))
            .appointmentStatus(AppointmentStatus.CONFIRMED)
            .appointmentAddress(address)
            .build();

        AppointmentDto appointmentDto = appointmentMapper.toDto(appointment);

        assertNotNull(appointmentDto);
        assertEquals(appointment.getId(), appointmentDto.id());
        assertEquals(appointment.getAppointmentDateTime(), appointmentDto.appointmentDateTime());
        assertEquals(appointment.getDuration(), appointmentDto.appointmentDuration());
        assertEquals(appointment.getPrice(), appointmentDto.price());
        assertEquals(appointment.getAppointmentStatus(), appointmentDto.appointmentStatus());
        assertEquals(appointment.getAppointmentAddress(), appointmentDto.address());
    }

    @Test
    void testToEntity() {
        Address address = new Address();
        AppointmentDto appointmentDto = AppointmentDto.builder()
            .id(UUID.randomUUID())
            .appointmentDateTime(LocalDateTime.now())
            .appointmentDuration(30)
            .price(BigDecimal.valueOf(100))
            .appointmentStatus(AppointmentStatus.CONFIRMED)
            .address(address)
            .build();

        Appointment appointment = appointmentMapper.toEntity(appointmentDto);

        assertNotNull(appointment);
        assertEquals(appointmentDto.id(), appointment.getId());
        assertEquals(appointmentDto.appointmentDateTime(), appointment.getAppointmentDateTime());
        assertEquals(appointmentDto.appointmentDuration(), appointment.getDuration());
        assertEquals(appointmentDto.price(), appointment.getPrice());
        assertEquals(appointmentDto.appointmentStatus(), appointment.getAppointmentStatus());
        assertEquals(appointmentDto.address(), appointment.getAppointmentAddress());
    }

    @Test
    void testUpdateAppointment() {
        Address address1 = new Address();
        Address address2 = new Address();

        Appointment appointment = Appointment.builder()
            .id(UUID.randomUUID())
            .appointmentDateTime(LocalDateTime.now())
            .duration(30)
            .price(BigDecimal.valueOf(100))
            .appointmentStatus(AppointmentStatus.CONFIRMED)
            .appointmentAddress(address1)
            .build();

        AppointmentDto appointmentDto = AppointmentDto.builder()
            .id(UUID.randomUUID())
            .appointmentDateTime(LocalDateTime.now().plusDays(1))
            .appointmentDuration(60)
            .price(BigDecimal.valueOf(200))
            .appointmentStatus(AppointmentStatus.CANCELLED)
            .address(address2)
            .build();

        appointmentMapper.updateAppointment(appointment, appointmentDto);

        assertEquals(appointmentDto.id(), appointment.getId());
        assertEquals(appointmentDto.appointmentDateTime(), appointment.getAppointmentDateTime());
        assertEquals(appointmentDto.appointmentDuration(), appointment.getDuration());
        assertEquals(appointmentDto.price(), appointment.getPrice());
        assertEquals(appointmentDto.appointmentStatus(), appointment.getAppointmentStatus());
        assertEquals(appointmentDto.address(), appointment.getAppointmentAddress());
    }
}
