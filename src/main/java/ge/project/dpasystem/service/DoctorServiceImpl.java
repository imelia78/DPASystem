package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.ClientDto;
import ge.project.dpasystem.dto.DoctorDto;
import ge.project.dpasystem.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService{

    private final DoctorRepository doctorRepository;
    //private final DoctorMapper doctorMapper;

    @Override
    public List<DoctorDto> findAllClientsByPages(RequestFilter filter) {
        return List.of();
    }

    @Override
    public DoctorDto findById(UUID uuid) {
        return null;
    }

    @Override
    public DoctorDto createClient(DoctorDto doctorDto) {
        return null;
    }

    @Override
    public ClientDto updateClient(DoctorDto doctorDto) {
        return null;
    }

    @Override
    public List<ClientDto> findClientsByFirstNameAndLastName(String firstName, String lastName) {
        return List.of();
    }

    @Override
    public ClientDto findClientByEmail(String email) {
        return null;
    }

    @Override
    public ClientDto updateEmail(UUID id, String email) {
        return null;
    }

    @Override
    public ClientDto updatePhoneNumber(UUID id, String phoneNumber) {
        return null;
    }

    @Override
    public void deleteByEmail(String email) {

    }
}
