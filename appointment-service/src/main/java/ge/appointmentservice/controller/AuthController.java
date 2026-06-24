package ge.appointmentservice.controller;



import ge.appointmentservice.dto.auth.AuthResponse;
import ge.appointmentservice.dto.auth.LoginRequest;
import ge.appointmentservice.dto.auth.RegisterClientRequest;
import ge.appointmentservice.dto.auth.RegisterDoctorRequest;
import ge.appointmentservice.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "auth_methods")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;


    @PostMapping("/register/doctor")
    public ResponseEntity<String> registerDoctor(@RequestBody RegisterDoctorRequest request) {
        authService.registerDoctorRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Your request has been send successfully!");

    }

    @PostMapping("/register/client")
    public ResponseEntity<String> registerClient(@RequestBody RegisterClientRequest request) {
        authService.registerClientRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Your profile has been created successfully!");
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        var token = authService.login(request);
        return ResponseEntity.ok(token);

    }

}
