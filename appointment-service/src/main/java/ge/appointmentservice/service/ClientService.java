package ge.appointmentservice.service;



import ge.appointmentservice.controller.RequestFilter;
import ge.appointmentservice.dto.ClientDto;
import ge.appointmentservice.dto.auth.RegisterClientRequest;

import java.util.List;
import java.util.UUID;

public interface ClientService {

    List<ClientDto> findAllClientsByPages(RequestFilter filter);

    ClientDto findClientById(UUID uuid);

    ClientDto createClient(RegisterClientRequest request, String keycloakId);

    ClientDto updateClient(ClientDto clientDTO);

    List<ClientDto> findClientsByFirstNameAndLastName(String firstName, String lastName);

    ClientDto findClientByEmail(String newEmail);

    ClientDto findClientByKeycloakUserId(String keycloakId);

    ClientDto updateEmail(UUID id, String email);

    ClientDto updatePhoneNumber(UUID id, String phoneNumber);

    void deleteClientByEmail(String email);

    void deleteClientById(UUID id);
}
