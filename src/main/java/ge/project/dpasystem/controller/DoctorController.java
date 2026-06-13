package ge.project.dpasystem.controller;

import ge.project.dpasystem.dto.DoctorDto;
import ge.project.dpasystem.dto.UpdatePhoneDto;
import ge.project.dpasystem.dto.UpdateProfessionalDescriptionDto;
import ge.project.dpasystem.dto.auth.RegisterDoctorRequest;
import ge.project.dpasystem.mapper.DoctorMapper;
import ge.project.dpasystem.service.DoctorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

@Tag(name = "doctor_methods")
@RestController
@RequestMapping("/api/v1/doctors")
@Slf4j
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;


    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
    ) {
        var filter = new RequestFilter(pageSize, pageNumber);
        var doctors = doctorService.findAllDoctorsByPages(filter);
        return ResponseEntity.ok(doctors);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable UUID id) {
        var doctor = doctorService.findDoctorById(id);
        return ResponseEntity.ok(doctor);

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(params = "email")
    public ResponseEntity<DoctorDto> getDoctorByEmail(@RequestParam String email) {
        var doctor = doctorService.findDoctorByEmail(email);
        return ResponseEntity.ok(doctor);
    }

    /*@PostMapping
    public ResponseEntity<DoctorDto> createDoctor(@RequestBody RegisterDoctorRequest request) {
        var newDoctor = doctorService.createDoctor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDoctor);
    }*/


    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.DOCTOR','dpasystem.DOCTOR_PENDING')")
    @PutMapping
    public ResponseEntity<DoctorDto> updateDoctor(@RequestBody DoctorDto doctorDto) {
        var doctor = doctorService.updateDoctor(doctorDto);
        return ResponseEntity.ok(doctor);
    }


    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.DOCTOR','dpasystem.DOCTOR_PENDING')")
    @PatchMapping("/{id}/about-me")
    public ResponseEntity<DoctorDto> updateDoctorProfDescription(@PathVariable UUID id,
                                                                 @RequestBody UpdateProfessionalDescriptionDto descriptionDto) {
        log.info("Description from request: {}",
                descriptionDto.professionalDescription());

        var doctor = doctorService.updateProfessionalDescription(id, descriptionDto.professionalDescription());

        return ResponseEntity.ok(doctor);
    }

    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.DOCTOR','dpasystem.DOCTOR_PENDING')")
    @PatchMapping("/{id}/phone")
    @Transactional
    public ResponseEntity<DoctorDto> updateDoctorPhoneNumber(@PathVariable UUID id, @RequestBody UpdatePhoneDto phoneDto) {
        var doctor = doctorService.updatePhoneNumber(id, phoneDto.phoneNumber());
        return ResponseEntity.ok(doctor);

    }
    @PreAuthorize("hasAuthority('dpasystem.ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteById(id);
        return ResponseEntity.ok("Doctor successfully deleted");
    }

}
