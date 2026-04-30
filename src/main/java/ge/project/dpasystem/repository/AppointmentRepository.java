package ge.project.dpasystem.repository;

import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.AppointmentStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    Page<Appointment> findAllBy(
            Pageable pageable
    );

    Optional<Appointment> findAppointmentById(UUID id);

    List<Appointment> findAllByClient_Id(UUID clientId, Pageable pageable); // All appointments related with particular client

    List<Appointment> findAllByDoctor_Id(UUID doctorId, Pageable pageable); // All appointments related with particular doctor

    List<Appointment> findAllByAppointmentStatus(AppointmentStatus appointmentStatus, Pageable pageable);

    List<Appointment> findAllByAppointmentDateTime(LocalDateTime appointmentDateTime);// как лучше передавать дату?

    List<Appointment> findAllByAppointmentAddress(String appointmentAddress);

    List<Appointment> findAllByAppointmentStatus(AppointmentStatus appointmentStatus);

    List<Appointment> findAppointmentsByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);

}
