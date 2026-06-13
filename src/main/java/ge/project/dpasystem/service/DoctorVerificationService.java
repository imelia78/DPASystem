package ge.project.dpasystem.service;

import ge.project.dpasystem.dto.AdminComment;
import ge.project.dpasystem.dto.DoctorDto;

import java.util.UUID;

public interface DoctorVerificationService {

    DoctorDto approveDoctorStatus(UUID id);

    DoctorDto rejectDoctorStatus(UUID id, AdminComment comment);

}
