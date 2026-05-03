package ge.project.dpasystem.controller;

import ge.project.dpasystem.dto.DoctorDto;
import ge.project.dpasystem.mapper.DoctorMapper;
import ge.project.dpasystem.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping()
    public ResponseEntity<DoctorDto> getDoctorByEmail(@RequestParam String email){
        var doctor = doctorService.findDoctorByEmail(email);
        return ResponseEntity.ok(doctor);
    }

    @PostMapping
    public ResponseEntity<DoctorDto> createDoctor(@RequestBody DoctorDto doctorDto){
        var newDoctor = doctorService.createDoctor(doctorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDoctor);
    }


}
