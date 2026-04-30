package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.ClientDto;
import ge.project.dpasystem.dto.DoctorDto;
import ge.project.dpasystem.model.Doctor;

import java.util.List;
import java.util.UUID;

public interface DoctorService {
    List<DoctorDto> findAllClientsByPages(RequestFilter filter);

    DoctorDto findById(UUID uuid);

    DoctorDto createClient(DoctorDto doctorDto);

    ClientDto updateClient(DoctorDto doctorDto);

    List<ClientDto> findClientsByFirstNameAndLastName(String firstName, String lastName);

    ClientDto findClientByEmail(String email);

    ClientDto updateEmail(UUID id, String email);

    ClientDto updatePhoneNumber(UUID id, String phoneNumber);

    void deleteByEmail(String email);
}
