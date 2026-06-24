package ge.appointmentservice.dto;


import ge.appointmentservice.model.AppointmentStatus;

public record UpdateAppointmentStatus(
        AppointmentStatus updatedStatus
) {
}
