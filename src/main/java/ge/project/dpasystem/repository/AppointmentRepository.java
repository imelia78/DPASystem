package ge.project.dpasystem.repository;

import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.AppointmentStatus;

import ge.project.dpasystem.model.Client;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    Page<Appointment> findAllBy(Pageable pageable);

    Optional<Appointment> findAppointmentById(UUID id);


    Boolean existsAppointmentByAppointmentDateTimeAndClient(LocalDateTime appointmentDateTime, Client client);


    List<Appointment> findAllByClient_Id(UUID clientId, Pageable pageable); // All appointments related with particular client

    List<Appointment> findAllByDoctor_Id(UUID doctorId, Pageable pageable); // All appointments related with particular doctor

    List<Appointment> findAllByAppointmentStatus(AppointmentStatus appointmentStatus, Pageable pageable);

    List<Appointment> findAllByAppointmentDateTime(LocalDateTime appointmentDateTime);// как лучше передавать дату?

    List<Appointment> findAllByAppointmentStatus(AppointmentStatus appointmentStatus);

    List<Appointment> findAppointmentsByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);

    Boolean existsByDoctorIdAndAppointmentDateTimeBetween(UUID doctorId, LocalDateTime start, LocalDateTime end);

    Boolean existsByClientIdAndAppointmentDateTimeBetween(UUID clientId, LocalDateTime start, LocalDateTime end);


    @Query(
            """ 
                            SELECT a FROM Appointment a
                            WHERE (:city IS NULL or a.appointmentAddress.city = :city)
                            AND (:district IS NULL or a.appointmentAddress.district = :district)
                            AND (:street IS NULL or a.appointmentAddress.street = :street)
                    """
    )
    List<Appointment> findAppointmentsByAddressFiltred(
            @Param("city") String city,
            @Param("district") String district,
            @Param("street") String street
    );


}
