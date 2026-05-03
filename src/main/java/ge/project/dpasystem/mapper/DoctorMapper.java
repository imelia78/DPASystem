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
                .age(doctorDto.age())
                .email(doctorDto.email())
                .specialization(doctorDto.specialization())
                .experience(doctorDto.experience())
                .appointments(doctorDto.appointments())
                .reviews(doctorDto.reviews())
                .build();
    }

    public DoctorDto toDto(Doctor doctor) {
        return DoctorDto.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .specialization(doctor.getSpecialization())
                .experience(doctor.getExperience())
                .age(doctor.getAge())
                .email(doctor.getEmail())
                .appointments(doctor.getAppointments())
                .reviews(doctor.getReviews())
                .build();
    }

    public void updateEntity(Doctor doctor, DoctorDto doctorDto) {
        Doctor.builder()
                .specialization(doctorDto.specialization())
                .experience(doctorDto.experience())
                .age(doctorDto.age())
                .email(doctorDto.email())
                .appointments(doctorDto.appointments())
                .reviews(doctorDto.reviews())
                .build();
    }

}
