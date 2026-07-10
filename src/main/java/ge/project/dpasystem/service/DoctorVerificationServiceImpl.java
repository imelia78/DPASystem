package ge.project.dpasystem.service;

import ge.project.dpasystem.dto.AdminComment;
import ge.project.dpasystem.dto.DoctorDto;
import ge.project.dpasystem.mapper.DoctorMapper;
import ge.project.dpasystem.model.Doctor;
import ge.project.dpasystem.model.VerificationStatus;
import ge.project.dpasystem.repository.DoctorRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorVerificationServiceImpl implements DoctorVerificationService{

    private final DoctorRepository doctorRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final DoctorMapper doctorMapper;



    @Override
    @Transactional
    public DoctorDto approveDoctorStatus(UUID id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (doctor.getVerificationStatus() == VerificationStatus.APPROVED){
            throw new IllegalArgumentException("Doctor already approved!");
        }
        String keycloakId = doctor.getKeycloakUserId();

        keycloakAdminService.removeRole(keycloakId, "dpasystem.DOCTOR_PENDING");
        keycloakAdminService.assignRole(keycloakId,"dpasystem.DOCTOR");
        log.info("New role assigned to the doctor {} in KeyCloak!", keycloakId);

        doctor.setVerificationStatus(VerificationStatus.APPROVED);
        log.info("Doctor {} has been verified successfully!",id);

        return doctorMapper.toDto(doctor);

    }

    @Override
    @Transactional
    public DoctorDto rejectDoctorStatus(UUID id, AdminComment comment) {

        Doctor doctor = doctorRepository.findById(id).orElseThrow(EntityExistsException::new);

        if (doctor.getVerificationStatus() != VerificationStatus.PENDING &&
                doctor.getVerificationStatus() != VerificationStatus.REJECTED){
            throw new IllegalArgumentException("Only pending review doctors can be rejected!");
        }

        doctor.setVerificationStatus(
                VerificationStatus.REJECTED
        );
        log.info("Doctor verification has been rejected!");

        doctor.setAdminComment(comment.comment());
        log.info("Comment about cause of rejection has been written!");

        return doctorMapper.toDto(doctor);

    }


}
