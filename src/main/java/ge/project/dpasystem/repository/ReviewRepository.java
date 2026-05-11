package ge.project.dpasystem.repository;

import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.Client;
import ge.project.dpasystem.model.Doctor;
import ge.project.dpasystem.model.Review;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {


    Page<Review> findAllBy( Pageable pageable);


    Page<Review> findAllByClientId(UUID clientId, Pageable pageable);

    Page<Review> findAllByDoctorId(UUID doctorId, Pageable pageable);

    Page<Review> findReviewsByCreatedAt(LocalDateTime createdAt,
                                        Pageable pageable);

    boolean existsByAppointment(Appointment appointment);
}
