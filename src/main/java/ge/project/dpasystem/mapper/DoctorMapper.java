package ge.project.dpasystem.mapper;

import ge.project.dpasystem.dto.DoctorDto;
import ge.project.dpasystem.dto.auth.RegisterDoctorRequest;
import ge.project.dpasystem.model.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public Doctor toEntity(DoctorDto doctorDto) {
        return Doctor.builder()
                .firstName(doctorDto.firstName())
                .lastName(doctorDto.lastName())
                .professionalDescription(doctorDto.professionalDescription())
                .sex(doctorDto.sex())
                .dateOfBirth(doctorDto.dateOfBirth())
                .phoneNumber(doctorDto.phoneNumber())
                .email(doctorDto.email())
                .specialization(doctorDto.specialization())
                .appointments(doctorDto.appointments())
                .reviews(doctorDto.reviews())
                .build();
    }

    public DoctorDto toDto(Doctor doctor) {
        return DoctorDto.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .sex(doctor.getSex())
                .phoneNumber(doctor.getPhoneNumber())
                .specialization(doctor.getSpecialization())
                .professionalDescription(doctor.getProfessionalDescription())
                .dateOfBirth(doctor.getDateOfBirth())
                .email(doctor.getEmail())
                .appointments(doctor.getAppointments())
                .adminComment(doctor.getAdminComment())
                .reviews(doctor.getReviews())
                .build();
    }
    public Doctor toEntity(RegisterDoctorRequest request){
        return Doctor.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .sex(request.sex())
                .dateOfBirth(request.dateOfBirth())
                .specialization(request.specialization())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .password(request.password())
                .professionalDescription(request.professionalDescription())
                .stateCertificateNumber(request.stateCertificateNumber())
                .build();
    }


    public void updateEntity(Doctor doctor, DoctorDto doctorDto) {
        doctor.setProfessionalDescription(doctorDto.professionalDescription());
        doctor.setProfessionalDescription(doctorDto.professionalDescription());
        doctor.setSpecialization(doctorDto.specialization());
        doctor.setPhoneNumber(doctorDto.phoneNumber());
        doctor.setEmail(doctorDto.email());
        doctor.setDateOfBirth(doctorDto.dateOfBirth());
    }

}
