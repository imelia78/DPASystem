package ge.appointmentservice.service;


import ge.appointmentservice.controller.RequestFilter;
import ge.appointmentservice.dto.DoctorDto;
import ge.appointmentservice.dto.auth.RegisterDoctorRequest;
import ge.appointmentservice.dto.kafka.DoctorRegisteredEvent;
import ge.appointmentservice.kafka.DoctorKafkaProducer;
import ge.appointmentservice.mapper.DoctorMapper;
import ge.appointmentservice.model.Doctor;
import ge.appointmentservice.model.VerificationStatus;
import ge.appointmentservice.repository.DoctorRepository;
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
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final KeycloakAdminService keycloakAdminService;
    private final DoctorKafkaProducer doctorKafkaProducer;

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
    @Transactional
    public DoctorDto createDoctor(RegisterDoctorRequest request, String keycloakId) {

        Doctor doctor = doctorMapper.toEntity(request);
        doctor.setKeycloakUserId(keycloakId);
        doctor.setVerificationStatus(VerificationStatus.PENDING);
        Doctor savedDoctor = doctorRepository.save(doctor);

        var event = new DoctorRegisteredEvent(
                doctor.getId(),
                doctor.getEmail(),
                doctor.getFirstName(),
                doctor.getLastName()
        );

        doctorKafkaProducer.sendDoctorRegisteredEventToKafka(event);

        return doctorMapper.toDto(savedDoctor);


    }

    @Override
    @Transactional
    public DoctorDto updateDoctor(DoctorDto doctorDto) {
        var doctor = doctorRepository.findById(doctorDto.id()).orElseThrow(EntityNotFoundException::new);

        doctorMapper.updateEntity(doctor, doctorDto);
        return doctorMapper.toDto(doctor);

    }

    @Override
    public List<DoctorDto> findDoctorsByFirstNameAndLastName(String firstName, String lastName) {
        var doctors = doctorRepository.findByFirstNameAndLastName(firstName, lastName);
        return doctors.stream().map(doctorMapper::toDto).toList();

    }


    @Override
    public DoctorDto findDoctorByEmail(String email) {
        var doctor = doctorRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return doctorMapper.toDto(doctor);
    }

    @Override
    public DoctorDto findDoctorByKeycloakUserId(String keycloakId) {
        var doctor = doctorRepository.findByKeycloakUserId(keycloakId).orElseThrow(EntityNotFoundException::new);
        return doctorMapper.toDto(doctor);
    }




    @Override
    @Transactional
    public DoctorDto updateEmail(UUID id, String newEmail) {


        var doctor = doctorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (doctorRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email already in use");
        }

        String oldEmail = doctor.getEmail();
        try {
            keycloakAdminService.updateUserEmail(
                    doctor.getKeycloakUserId(),
                    newEmail
            );
        } catch (Exception e) {
            doctor.setEmail(oldEmail);
            throw new IllegalArgumentException("Failed to update email in Keycloak",
                    e);
        }
        log.info("Email updated for doctor {}", doctor.getKeycloakUserId());

        doctor.setEmail(newEmail);
        return doctorMapper.toDto(doctor);
    }


    @Override
    @Transactional
    public DoctorDto updateProfessionalDescription(UUID id, String professionalDescription) {
        var doctor = doctorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        doctor.setProfessionalDescription(professionalDescription);
        return doctorMapper.toDto(doctor);
    }


    @Override
    @Transactional
    public DoctorDto updatePhoneNumber(UUID id, String phoneNumber) {
        var doctor = doctorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (doctorRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone already in use");
        }
        doctor.setPhoneNumber(phoneNumber);
        return doctorMapper.toDto(doctor);
    }


    @Override
    public void deleteByEmail(String email) {
        var doctor = doctorRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        doctorRepository.deleteById(doctor.getId());
    }

    @Override
    public void deleteById(UUID id) {
        var doctor = doctorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        doctorRepository.deleteById(id);

    }

}
