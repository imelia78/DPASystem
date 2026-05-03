package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.DoctorDto;
import ge.project.dpasystem.mapper.DoctorMapper;
import ge.project.dpasystem.model.Doctor;
import ge.project.dpasystem.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService{

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public List<DoctorDto> findAllDoctorsByPages(RequestFilter filter) {

        int pageSize = filter.pageSize() != null
                ? filter.pageSize() : 10; //todo default size in app.properties,  не хардкодить!
        int pageNumber = filter.pageNumber() != null
                ? filter.pageNumber() : 0; // first page
        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        var doctors = doctorRepository.findAllBy(pageable);

        return doctors.stream().map(doctorMapper::toDto).toList();
    }

    @Override
    public DoctorDto findDoctorById(UUID id) {
        return doctorMapper.toDto(doctorRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public DoctorDto createDoctor(DoctorDto doctorDto) {
        Doctor doctor = doctorRepository.save(doctorMapper.toEntity(doctorDto));
        return doctorMapper.toDto(doctor);

    }

    @Override
    @Transactional
    public DoctorDto updateDoctor(DoctorDto doctorDto) {
        var doctor = doctorRepository.findById(doctorDto.id()).orElseThrow(EntityNotFoundException::new);

        doctorMapper.updateEntity(doctor,doctorDto);
        return doctorMapper.toDto(doctor);

    }

    @Override
    public List<DoctorDto> findDoctorsByFirstNameAndLastName(String firstName, String lastName) {
        var doctors = doctorRepository.findByFirstNameAndLastName(firstName,lastName);
        return doctors.stream().map(doctorMapper::toDto).toList();

    }


    @Override
    public DoctorDto findDoctorByEmail(String email) {
        var doctor = doctorRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return doctorMapper.toDto(doctor);
    }

    @Override
    public DoctorDto updateEmail(UUID id, String email) {
        var doctor = doctorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        doctor.setEmail(email);
        return doctorMapper.toDto(doctor);
    }

   /* @Override
    public DoctorDto updatePhoneNumber(UUID id, String phoneNumber) {
        var doctor = doctorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        doctor.set
    }*/

    @Override
    public void deleteByEmail(String email) {
    var doctor = doctorRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    doctorRepository.deleteById(doctor.getId());
    }

}
