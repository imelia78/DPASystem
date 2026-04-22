package ge.project.dpasystem.repository;

import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    Client findClientByAppointments(List<Appointment> appointments);

    List<Client> findAllClientsByPages(
            Pageable pageable
    );

    List<Client> findClientByFirstNameAndLastName(String firstName, String lastName);

    Optional<Client> findClientById(UUID id);

    Optional<Client> findClientByEmail(String email);

    Client deleteByEmail(String email);
}

