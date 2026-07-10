package ge.project.dpasystem.dto;

import ge.project.dpasystem.model.AppointmentStatus;

public record UpdateAppointmentStatus(
        AppointmentStatus updatedStatus
) {
}
