package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.*;
import ge.project.dpasystem.mapper.AppointmentMapper;
import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.AppointmentStatus;
import ge.project.dpasystem.repository.AppointmentRepository;
import ge.project.dpasystem.repository.ClientRepository;
import ge.project.dpasystem.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final ClientRepository clientRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public List<AppointmentDto> findAllAppointmentsByPages(RequestFilter filter) {
        int pageSize = filter.pageSize() != null
                ? filter.pageSize() : 10; //todo default size in app.properties,  не хардкодить!
        int pageNumber = filter.pageNumber() != null
                ? filter.pageNumber() : 0; // first page
        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        return appointmentRepository.findAllBy(pageable).stream().map(appointmentMapper::toDto).toList();
    }

    @Override
    public List<AppointmentDto> findAllAppointmentsByClientId(UUID clientId, RequestFilter filter) {
        int pageSize = filter.pageSize() != null
                ? filter.pageSize() : 10; //todo default size in app.properties,  не хардкодить!
        int pageNumber = filter.pageNumber() != null
                ? filter.pageNumber() : 0; // first page
        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        var clientAppointments = appointmentRepository.findAllByClient_Id(clientId, pageable);
        return clientAppointments.stream().map(appointmentMapper::toDto).toList();
    }

    @Override
    public AppointmentDto createAppointment(AppointmentRequestDto request) {
        var doctor = doctorRepository.findById(request.doctor().id())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found!"));
        var client = clientRepository.findClientById(request.client().id())
                .orElseThrow(() -> new EntityNotFoundException("Client not found!"));

        bookingConflictsExclusion(doctor.getId(), client.getId(), request.dateTime(), request.duration());

        var appointment = Appointment.builder()
                .appointmentDateTime(request.dateTime())
                .duration(request.duration())
                .client(client)
                .appointmentStatus(AppointmentStatus.CREATED)
                .doctor(doctor)
                .appointmentAddress(request.address())
                .build();

        var saved = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(saved);
    }


    public void bookingConflictsExclusion(UUID doctorId, UUID clientId, LocalDateTime start, int duration) {

        LocalDateTime end = start.plusMinutes(duration);

        boolean doctorIsBusy = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeBetween(doctorId, start, end);

        if (doctorIsBusy) {
            throw new IllegalStateException("Doctor has been busy at this time");
        }

        boolean clientIsBusy = appointmentRepository.existsByClientIdAndAppointmentDateTimeBetween(clientId, start, end);
        if (clientIsBusy) {
            throw new IllegalStateException("Client already has an appointment");
        }
    }


    @Override
    public AppointmentDto updateAppointment(AppointmentDto appointmentDto) { // не уверен будет ли работать

        var updAppointment = appointmentRepository.findAppointmentById(appointmentDto.id()).orElseThrow(EntityNotFoundException::new);
        log.info("updating appointment with id: {}", appointmentDto.id());

        appointmentMapper.updateAppointment(updAppointment, appointmentDto);
        return appointmentMapper.toDto(updAppointment);
    }


    @Override
    @Transactional
    public AppointmentDto updateAppointmentStatus(UUID id, UpdateAppointmentStatus request) {

        var updAppointment = appointmentRepository.findAppointmentById(id).orElseThrow(EntityNotFoundException::new);

        log.info("updating status for appointment with id: {}", id);

        updAppointment.setAppointmentStatus(request.updatedStatus());

        return appointmentMapper.toDto(updAppointment);

    }


    @Override
    @Transactional
    public AppointmentDto updateAppointmentDateOrTime(UUID id, UpdateAppointmentDateTime request) {  //rescheduling

        if (request.dateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("date has already passed!"); // Сделать на уровне валидации через аннотации?
        }

        Appointment appointment = appointmentRepository.findAppointmentById(id).orElseThrow(EntityNotFoundException::new);

        var doctor = appointment.getDoctor();
        var client = appointment.getClient();

        LocalDateTime start = request.dateTime();

        bookingConflictsExclusion(doctor.getId(), client.getId(), start, request.duration());

        appointment.setAppointmentDateTime(request.dateTime());
        appointment.setDuration(request.duration());

        return appointmentMapper.toDto(appointment);

    }

    @Override
    public List<AppointmentDto> findAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Invalid date range");
        }

        var appointments = appointmentRepository.findAppointmentsByAppointmentDateTimeBetween(start, end);

        return appointments.stream().map(appointmentMapper::toDto).toList();
    }

    @Override
    public List<AppointmentDto> findAppointmentsByStatus(AppointmentStatus status) {
        var appointments = appointmentRepository.findAllByAppointmentStatus(status);
        return appointments.stream().map(appointmentMapper::toDto).toList();
    }

    @Override
    public List<AppointmentDto> findAppointmentsByAddress(AddressDto addressDto) {
        var appointments = appointmentRepository.findAppointmentsByAddressMixed(
                addressDto.city(), addressDto.district(), addressDto.street());
        return appointments.stream().map(appointmentMapper::toDto).toList();

    }

    @Override
    public void processAppointment(UUID id) {
        var appointment = appointmentRepository.findAppointmentById(id).orElseThrow(EntityNotFoundException::new);
        appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
        log.info("Appointment with id {} completed successfully", id);


    }
}
