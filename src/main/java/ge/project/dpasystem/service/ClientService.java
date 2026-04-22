package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.ClientDto;

import java.util.List;
import java.util.UUID;

public interface ClientService {

    List<ClientDto> findAllClientsByPages(RequestFilter filter);

    ClientDto findClientById(UUID uuid);

    ClientDto createClient(ClientDto clientDTO);

    ClientDto updateClient(ClientDto clientDTO);

    List<ClientDto> findClientsByFirstNameAndLastName(String firstName, String lastName);

    ClientDto findClientByEmail(String email);

    void deleteClientByEmail(String email);
}
