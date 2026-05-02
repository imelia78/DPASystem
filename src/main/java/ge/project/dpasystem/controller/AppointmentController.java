package ge.project.dpasystem.controller;

import ge.project.dpasystem.dto.AddressDto;
import ge.project.dpasystem.dto.AppointmentDto;
import ge.project.dpasystem.dto.UpdateAppointmentDateTime;
import ge.project.dpasystem.dto.UpdateAppointmentStatus;
import ge.project.dpasystem.mapper.AppointmentMapper;
import ge.project.dpasystem.model.AppointmentStatus;
import ge.project.dpasystem.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
    ) {
        var filter = new RequestFilter(pageSize, pageNumber);
        return ResponseEntity.ok(appointmentService.findAllAppointmentsByPages(filter));
    }

// TODO Нужно ли менять типь возращаемого значения в сигнатуре на pageable?


    @GetMapping("/clients/{id}/appointments")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByClient(
            @PathVariable UUID id,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
    ) {
        var filter = new RequestFilter(pageSize, pageNumber);
        return ResponseEntity.ok(appointmentService.findAllAppointmentsByClientId(id, filter));

    }

   /* @GetMapping()
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByAddress(@RequestBody AddressDto address) {
        return ResponseEntity.ok(appointmentService.findAppointmentsByAddress());

    }*/

    @GetMapping("/status")
    public ResponseEntity<List<AppointmentDto>> getAppointmentByStatus(@RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.findAppointmentsByStatus(status));
    }


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


    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto appointmentDto){
        return ResponseEntity.ok(appointmentService.createAppointment(appointmentDto));
    }


    @PatchMapping("/{id}/status")
    ResponseEntity<AppointmentDto> updateAppointmentStatus(@PathVariable UUID id,
        @RequestBody UpdateAppointmentStatus request){
        return  ResponseEntity.ok(appointmentService.updateAppointmentStatus(id,request));

    }

    @PatchMapping("{id}/datetime")
    ResponseEntity<AppointmentDto> updateAppointmentDateOrTime(@PathVariable UUID id,
        @RequestBody UpdateAppointmentDateTime request){
        return ResponseEntity.ok(appointmentService.updateAppointmentDateOrTime(id,request));
    }




    @PutMapping("{id}")
    ResponseEntity<AppointmentDto> updateAppointment(@PathVariable UUID id,
        @RequestBody AppointmentDto appointmentDto){
        return ResponseEntity.ok(appointmentService.updateAppointment(appointmentDto));
    }


}
