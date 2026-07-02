package ge.appointmentservice.controller;


import ge.appointmentservice.dto.*;
import ge.appointmentservice.model.AppointmentStatus;
import ge.appointmentservice.service.AppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "appointment_methods")
@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @PreAuthorize("hasAuthority('dpasystem.ADMIN')")
    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
    ) {
        var filter = new RequestFilter(pageSize, pageNumber);
        return ResponseEntity.ok(appointmentService.findAllAppointmentsByPages(filter));
    }

// TODO Нужно ли менять типь возращаемого значения в сигнатуре на pageable?


    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.CLIENT')")
    @GetMapping("/clients/{id}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByClientId(
            @PathVariable UUID id,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
    ) {
        var filter = new RequestFilter(pageSize, pageNumber);
        return ResponseEntity.ok(appointmentService.findAllAppointmentsByClientId(id, filter));

    }


    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.DOCTOR')")
    @GetMapping("/status")
    public ResponseEntity<List<AppointmentDto>> getAppointmentByStatus(@RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.findAppointmentsByStatus(status));
    }


    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.CLIENT')")
    @GetMapping("/client/{id}/upcoming")
    public ResponseEntity<List<AppointmentDto>> getUpcomingAppointments(
            @PathVariable UUID id
    ){
        return ResponseEntity.ok(appointmentService.findUpcomingAppointments(id));
    }


    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.CLIENT')")
    @GetMapping("/client/{id}/history")
    public ResponseEntity<List<AppointmentDto>> getPreviousAppointments(@PathVariable UUID id){
        return ResponseEntity.ok(appointmentService.findPreviousAppointments(id));
    }


    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.DOCTOR')")
    @GetMapping("/date-range")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDateRange(
            @RequestParam
            @DateTimeFormat(pattern = "dd-MM-yyyy")
            LocalDateTime start,

            @RequestParam
            @DateTimeFormat(pattern = "dd-MM-yyyy")
            LocalDateTime end
    ) {
        return ResponseEntity.ok(appointmentService.findAppointmentsByDateRange(start, end));
    }


    @PreAuthorize("hasAuthority('dpasystem.ADMIN')")
    @GetMapping("/address")
    public ResponseEntity<List<AppointmentDto>> getAppointmentByAddress(
            @ModelAttribute AddressDto addressDto
    ) {
        return ResponseEntity.ok(appointmentService.findAppointmentsByAddress(addressDto));
    }


    @PreAuthorize("hasAuthority('dpasystem.CLIENT')")
    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentRequestDto request) {
        return ResponseEntity.ok(appointmentService.createAppointment(request));
    }


    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.DOCTOR')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDto> updateAppointmentStatus(@PathVariable UUID id, @RequestBody UpdateAppointmentStatus request) {
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id, request));

    }


    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.DOCTOR','dpasystem.CLIENT')")
    @PatchMapping("{id}/datetime")
    public ResponseEntity<AppointmentDto> updateAppointmentDateOrTime(@PathVariable UUID id, @RequestBody UpdateAppointmentDateTime request) {
        return ResponseEntity.ok(appointmentService.updateAppointmentDateOrTime(id, request));
    }


    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.DOCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable UUID id, @RequestBody AppointmentDto appointmentDto) {
        return ResponseEntity.ok(appointmentService.updateAppointment(appointmentDto));
    }


    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.CLIENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointmentById(@PathVariable UUID id) {
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.ok("Appointment canceled successfully!");

    }


}
