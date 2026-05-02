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
                .appointmentDuration(appointment.getAppointmentDuration())
                .price(appointment.getPrice())
                .appointmentStatus(appointment.getAppointmentStatus())
                .appointmentAddress(appointment.getAppointmentAddress())
                .review(appointment.getReview())
                .doctor(appointment.getDoctor())
                .client(appointment.getClient())
                .description(appointment.getDescription())
                .build();

    }


    public Appointment toAppointment(AppointmentDto appointmentDto) {
        return Appointment.builder()
            .id(appointmentDto.id())              // ← add this line
            .appointmentDateTime(appointmentDto.appointmentDateTime())
            .appointmentAddress(appointmentDto.appointmentAddress())
            .price(appointmentDto.price())
            .appointmentDuration(appointmentDto.appointmentDuration())
            .appointmentStatus(appointmentDto.appointmentStatus())
            .client(appointmentDto.client())
            .doctor(appointmentDto.doctor())
            .review(appointmentDto.review())
            .description(appointmentDto.description())
            .build();
    }

    public void updateAppointment(Appointment appointment, AppointmentDto appointmentDto) {
        appointment.setId(appointmentDto.id());
        appointment.setAppointmentStatus(appointmentDto.appointmentStatus());
        appointment.setAppointmentAddress(appointmentDto.appointmentAddress());
        appointment.setAppointmentDateTime(appointmentDto.appointmentDateTime());
        appointment.setClient(appointmentDto.client());
        appointment.setDoctor(appointmentDto.doctor());
        appointment.setPrice(appointmentDto.price());
        appointment.setAppointmentDuration(appointmentDto.appointmentDuration());
        appointment.setReview(appointmentDto.review());
        appointment.setDescription(appointmentDto.description());

    }

}
