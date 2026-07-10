package ge.project.dpasystem.mapper;


import ge.project.dpasystem.dto.AppointmentDto;
import ge.project.dpasystem.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {


    public AppointmentDto toDto(Appointment appointment) {

        return AppointmentDto.builder()
                .id(appointment.getId())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .appointmentDuration(appointment.getDuration())
                .price(appointment.getPrice())
                .appointmentStatus(appointment.getAppointmentStatus())
                .address(appointment.getAppointmentAddress())
                .review(appointment.getReview())
                .doctor(appointment.getDoctor())
                .client(appointment.getClient())
                .build();

    }


    public Appointment toEntity(AppointmentDto appointmentDto) {

        return Appointment.builder()
                .appointmentDateTime(appointmentDto.appointmentDateTime())
                .appointmentAddress(appointmentDto.address())
                .price(appointmentDto.price())
                .duration(appointmentDto.appointmentDuration())
                .appointmentStatus(appointmentDto.appointmentStatus())
                .client(appointmentDto.client())
                .doctor(appointmentDto.doctor())
                .review(appointmentDto.review())
                .build();

    }

    public void updateAppointment(Appointment appointment, AppointmentDto appointmentDto) {
        appointment.setId(appointmentDto.id());
        appointment.setAppointmentStatus(appointmentDto.appointmentStatus());
        appointment.setAppointmentAddress(appointmentDto.address());
        appointment.setAppointmentDateTime(appointmentDto.appointmentDateTime());
        appointment.setClient(appointmentDto.client());
        appointment.setDoctor(appointmentDto.doctor());
        appointment.setPrice(appointmentDto.price());
        appointment.setDuration(appointmentDto.appointmentDuration());
        appointment.setReview(appointmentDto.review());

    }

}
