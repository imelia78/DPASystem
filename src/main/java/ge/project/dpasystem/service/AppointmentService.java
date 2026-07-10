package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.*;
import ge.project.dpasystem.model.Address;
import ge.project.dpasystem.model.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    List<AppointmentDto> findAllAppointmentsByPages(RequestFilter filter);

    List<AppointmentDto> findAllAppointmentsByClientId(UUID clientId, RequestFilter filter);

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
