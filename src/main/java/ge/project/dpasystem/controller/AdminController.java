package ge.project.dpasystem.controller;

import ge.project.dpasystem.dto.AdminComment;
import ge.project.dpasystem.dto.DoctorDto;
import ge.project.dpasystem.model.Doctor;
import ge.project.dpasystem.service.DoctorVerificationService;
import ge.project.dpasystem.service.KeycloakAdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "admin_methods")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DoctorVerificationService doctorVerificationService;



    @PatchMapping("/{id}/approve")
    public ResponseEntity<DoctorDto> verifyDoctorStatus(@PathVariable UUID id) {

        return ResponseEntity.ok(doctorVerificationService.approveDoctorStatus(id));

    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<DoctorDto> rejectDoctorStatus(@PathVariable UUID id, @RequestBody AdminComment adminComment){

        return ResponseEntity.ok(doctorVerificationService.rejectDoctorStatus(id, adminComment));
    }


}
