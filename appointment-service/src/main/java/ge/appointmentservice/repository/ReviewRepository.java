package ge.appointmentservice.repository;


import ge.appointmentservice.model.Appointment;
import ge.appointmentservice.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {


    Page<Review> findAllBy( Pageable pageable);


    Page<Review> findAllByClientId(UUID clientId, Pageable pageable);

    Page<Review> findAllByDoctorId(UUID doctorId, Pageable pageable);

    Page<Review> findReviewsByCreatedAt(LocalDateTime createdAt,
                                        Pageable pageable);

    boolean existsByAppointment(Appointment appointment);
}
