package ge.appointmentservice.service;


import ge.appointmentservice.dto.AdminComment;
import ge.appointmentservice.dto.DoctorDto;

import java.util.UUID;

public interface DoctorVerificationService {

    DoctorDto approveDoctorStatus(UUID id);

    DoctorDto rejectDoctorStatus(UUID id, AdminComment comment);

}
