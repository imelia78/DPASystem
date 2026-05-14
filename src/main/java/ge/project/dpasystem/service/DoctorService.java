package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.DoctorDto;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    List<DoctorDto> findAllDoctorsByPages(RequestFilter filter);

    DoctorDto findDoctorById(UUID uuid);

    DoctorDto createDoctor(DoctorDto doctorDto);

    DoctorDto updateDoctor(DoctorDto doctorDto);

    DoctorDto updateProfessionalDescription(UUID id, String professionalDescription);

    List<DoctorDto> findDoctorsByFirstNameAndLastName(String firstName, String lastName);

    DoctorDto findDoctorByEmail(String email);

    DoctorDto updateEmail(UUID id, String email);

    DoctorDto updatePhoneNumber(UUID id, String phoneNumber);

    void deleteByEmail(String email);

    void deleteById(UUID id);
}
