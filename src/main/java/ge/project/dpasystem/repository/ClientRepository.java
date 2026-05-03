package ge.project.dpasystem.repository;

import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findClientByAppointments(List<Appointment> appointments);

    Page<Client> findAllBy(Pageable pageable);

    List<Client> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Client> findClientById(UUID id);

    Optional<Client> findByEmail(String email);

    Optional<Client> deleteByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}

