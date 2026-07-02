package ge.appointmentservice.repository;


import ge.appointmentservice.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {


    Page<Doctor> findAllBy(Pageable pageable);

    List<Doctor> findByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    Optional<Doctor> findById(UUID id);

    Optional<Doctor> findByEmail(String email);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
            value = """
                    UPDATE doctors d
                    SET average_rating = COALESCE(
                                ( SELECT AVG(r.rating) FROM reviews where r.doctor_id = :doctorId), 0),
                                reviews_count = (SELECT COUNT(*) FROM reviews r WHERE r.doctor_id = :doctorId) 
                                WHERE d.id = :doctorId
                    """, nativeQuery = true)
    void recalculateRating(@Param("doctorId") UUID doctorId);

    // List<Doctor> findClosestByFreeTime(LocalDateTime time);

    List<Doctor> findAllBySpecialization(String specialization, Pageable pageable);


    Optional<Doctor> findByKeycloakUserId(String keycloakUserId);
}
