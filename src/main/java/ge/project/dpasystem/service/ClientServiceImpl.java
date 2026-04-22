package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.ClientDto;
import ge.project.dpasystem.mapper.ClientMapper;
import ge.project.dpasystem.model.Client;
import ge.project.dpasystem.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {


    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public List<ClientDto> findAllClientsByPages(RequestFilter filter) {

        int pageSize = filter.pageSize() != null
                ? filter.pageSize() : 10; //todo default size in app.properties,  не хардкодить!
        int pageNumber = filter.pageNumber() != null
                ? filter.pageNumber() : 0; // first page
        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        return clientRepository.findAllClientsByPages(pageable).stream().map(clientMapper::toDto).toList();
    }

    @Override
    public ClientDto findClientById(UUID uuid) {
        Client foundClient = clientRepository.findClientById(uuid).orElseThrow(EntityNotFoundException::new);
        return clientMapper.toDto(foundClient);
    }

    @Override
    public ClientDto createClient(ClientDto clientDTO) {
        var createdClient = clientMapper.toEntity(clientDTO);
        return clientMapper.toDto(clientRepository.save(createdClient));

    }

    @Transactional
    @Override
    public ClientDto updateClient(ClientDto clientDto) { //TODO написать правильную логику обновления сущности
        Client updatedClient = clientRepository.findClientByEmail(clientDto.email()).orElseThrow(() ->
                new EntityNotFoundException("Client with email: " + clientDto.email() + " not found!"));
        log.info("Updating client with email: {}", clientDto.email());

        return clientMapper.
    }

    @Override
    public List<ClientDto> findClientsByFirstNameAndLastName(String firstName, String lastName) {
        return clientRepository.findClientByFirstNameAndLastName(firstName, lastName)
                .stream().map(clientMapper::toDto).toList();
    }

    @Override
    public ClientDto findClientByEmail(String email) {
        Client foundClient = clientRepository.findClientByEmail(email).orElseThrow(EntityNotFoundException::new);
        return clientMapper.toDto(foundClient);
    }

    @Override
    public void deleteClientByEmail(String email) {
        Client prepareForDeleting = clientRepository.findClientByEmail(email).orElseThrow(EntityNotFoundException::new);
        log.info("Client with email {} ready for deleting", email);
        Client deletedClient = clientRepository.deleteByEmail(email);

    }
}
