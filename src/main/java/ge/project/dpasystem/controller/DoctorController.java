package ge.project.dpasystem.controller;

import ge.project.dpasystem.dto.DoctorDto;
import ge.project.dpasystem.dto.UpdatePhoneDto;
import ge.project.dpasystem.dto.UpdateProfessionalDescriptionDto;
import ge.project.dpasystem.mapper.DoctorMapper;
import ge.project.dpasystem.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;


    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
    ) {
        var filter = new RequestFilter(pageSize, pageNumber);
        var doctors = doctorService.findAllDoctorsByPages(filter);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable UUID id) {
        var doctor = doctorService.findDoctorById(id);
        return ResponseEntity.ok(doctor);

    }

    @GetMapping(params = "email")
    public ResponseEntity<DoctorDto> getDoctorByEmail(@RequestParam String email) {
        var doctor = doctorService.findDoctorByEmail(email);
        return ResponseEntity.ok(doctor);
    }

    @PostMapping
    public ResponseEntity<DoctorDto> createDoctor(@RequestBody DoctorDto doctorDto) {
        var newDoctor = doctorService.createDoctor(doctorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDoctor);
    }

    @PutMapping
    public ResponseEntity<DoctorDto> updateDoctor(@RequestBody DoctorDto doctorDto) {
        var doctor = doctorService.updateDoctor(doctorDto);
        return ResponseEntity.ok(doctor);
    }

    @PatchMapping("/{id}/about-me")
    public ResponseEntity<DoctorDto> updateDoctorProfDescription(@PathVariable UUID id, @RequestBody UpdateProfessionalDescriptionDto descriptionDto) {
        var doctor = doctorService.updateProfessionalDescription(id, descriptionDto.professionalDescription());
        return ResponseEntity.ok(doctor);
    }

    @PatchMapping("/{id}/phone")
    public ResponseEntity<DoctorDto> updateDoctorPhoneNumber(@PathVariable UUID id, @RequestBody UpdatePhoneDto phoneDto) {
        var doctor = doctorService.updatePhoneNumber(id, phoneDto.phoneNumber());
        return ResponseEntity.ok(doctor);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteById(id);
        return ResponseEntity.ok("Doctor successfully deleted");
    }

}
