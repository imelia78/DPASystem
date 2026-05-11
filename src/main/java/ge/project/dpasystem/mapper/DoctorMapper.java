package ge.project.dpasystem.mapper;

import ge.project.dpasystem.dto.DoctorDto;
import ge.project.dpasystem.model.Doctor;
import org.springframework.stereotype.Component;

import javax.print.Doc;

@Component
public class DoctorMapper {

    public Doctor toEntity(DoctorDto doctorDto) {
        return Doctor.builder()
                .id(doctorDto.id())
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
                .reviews(doctor.getReviews())
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
