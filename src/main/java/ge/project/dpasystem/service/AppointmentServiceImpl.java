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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
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
    public AppointmentDto createAppointment(AppointmentDto appointmentDto) { // нужна ли какая-то логика на existBy??
        var created = appointmentMapper.toAppointment(appointmentDto);
        return appointmentMapper.toDto(appointmentRepository.save(created));
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
    public AppointmentDto updateAppointmentDateOrTime(UUID id, UpdateAppointmentDateTime request) {

        var updAppointment = appointmentRepository.findAppointmentById(id).orElseThrow(EntityNotFoundException::new);

        if (request.dateTime().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("date has already passed!");
        }
        updAppointment.setAppointmentDateTime(request.dateTime());
       return appointmentMapper.toDto(updAppointment);

    }

    @Override
    public List<AppointmentDto> findAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        if(start.isAfter(end)){
            throw new IllegalArgumentException("Invalid date range");
        }

        var appointments = appointmentRepository.findAppointmentsByAppointmentDateTimeBetween(start,end);

        return appointments.stream().map(appointmentMapper::toDto).toList();
    }

    @Override
    public List<AppointmentDto> findAppointmentsByStatus(AppointmentStatus status) {
        var appointments = appointmentRepository.findAllByAppointmentStatus(status);
        return appointments.stream().map(appointmentMapper::toDto).toList();
    }

    @Override
    public List<AppointmentDto> findAppointmentsByAddress(String address) {
        var appointments = appointmentRepository.findAllByAppointmentAddress(address);
        return appointments.stream().map(appointmentMapper::toDto).toList();

    }
}
