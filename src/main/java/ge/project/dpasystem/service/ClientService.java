package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.ClientDto;
import ge.project.dpasystem.dto.auth.RegisterClientRequest;

import java.util.List;
import java.util.UUID;

public interface ClientService {

    List<ClientDto> findAllClientsByPages(RequestFilter filter);

    ClientDto findClientById(UUID uuid);

    ClientDto createClient(RegisterClientRequest request, String keycloakId);

    ClientDto updateClient(ClientDto clientDTO);

    List<ClientDto> findClientsByFirstNameAndLastName(String firstName, String lastName);

    ClientDto findClientByEmail(String email);

    ClientDto findClientByKeycloakUserId(String keycloakId);

    ClientDto updateEmail(UUID id, String email);

    ClientDto updatePhoneNumber(UUID id, String phoneNumber);

    void deleteClientByEmail(String email);

    void deleteClientById(UUID id);
}
