package ge.appointmentservice.controller;

public record RequestFilter(
        Integer pageSize,
        Integer pageNumber
) {
}
