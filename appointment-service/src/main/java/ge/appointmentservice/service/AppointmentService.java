package ge.appointmentservice.service;



import ge.appointmentservice.controller.RequestFilter;
import ge.appointmentservice.dto.*;
import ge.appointmentservice.model.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    List<AppointmentDto> findAllAppointmentsByPages(RequestFilter filter);

    List<AppointmentDto> findAllAppointmentsByClientId(UUID clientId, RequestFilter filter);

    List<AppointmentDto> findUpcomingAppointments(UUID id);

    List<AppointmentDto> findPreviousAppointments(UUID id);

    AppointmentDto createAppointment(AppointmentRequestDto request);

    AppointmentDto updateAppointment(AppointmentDto appointmentDto);

    AppointmentDto updateAppointmentStatus(UUID id, UpdateAppointmentStatus request);

    AppointmentDto updateAppointmentDateOrTime(UUID id, UpdateAppointmentDateTime request);

   List<AppointmentDto> findAppointmentsByDateRange(LocalDateTime start, LocalDateTime end);

    List<AppointmentDto> findAppointmentsByStatus(AppointmentStatus status);

    List<AppointmentDto> findAppointmentsByAddress(AddressDto addressDto);

    void deleteAppointmentById(UUID id);

    void processAppointment(UUID id);

}
