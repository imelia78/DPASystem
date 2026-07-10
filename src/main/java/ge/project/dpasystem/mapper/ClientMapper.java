package ge.project.dpasystem.mapper;

import ge.project.dpasystem.dto.ClientDto;
import ge.project.dpasystem.dto.auth.RegisterClientRequest;
import ge.project.dpasystem.model.Client;
import org.springframework.stereotype.Component;


@Component
public class ClientMapper {

    public Client toEntity(ClientDto clientDto) {

        return Client.builder()
                .firstName(clientDto.firstName())
                .lastName(clientDto.lastName())
                .sex(clientDto.sex())
                .email(clientDto.email())
                .password(clientDto.password())
                .appointments(clientDto.appointments())
                .dateOfBirth(clientDto.dateOfBirth())
                .reviews(clientDto.reviews())
                .phoneNumber(clientDto.phoneNumber())
                .build();
    }

    public Client toEntity(RegisterClientRequest request){

        return Client.builder().
                firstName(request.firstName())
                .lastName(request.lastName())
                .dateOfBirth(request.dateOfBirth())
                .email(request.email())
                .password(request.password())
                .sex(request.sex())
                .phoneNumber(request.phoneNumber())
                .build();
    }


    public ClientDto toDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .sex(client.getSex())
                .dateOfBirth(client.getDateOfBirth())
                .password(client.getPassword())
                .reviews(client.getReviews())
                .appointments(client.getAppointments())
                .email(client.getEmail())
                .phoneNumber(client.getPhoneNumber())
                .build();

    }

    public void updateEntity(Client client, ClientDto clientDto) {
        client.setFirstName(clientDto.firstName());
        client.setLastName(clientDto.lastName());
        client.setDateOfBirth(clientDto.dateOfBirth());
        client.setPhoneNumber(clientDto.phoneNumber());
    }

}
