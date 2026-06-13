package ge.project.dpasystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;



@Entity
@Table(name = "doctors")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Doctor {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;


    @Column(name = "keycloak_user_id")
    private String keycloakUserId;


    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;


    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String adminComment;


    @Column(nullable = false)
    private String professionalDescription;


    @Column(nullable = false, unique = true)
    private String stateCertificateNumber;


    @Enumerated(EnumType.STRING)
    private  VerificationStatus verificationStatus;


    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;


    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Review> reviews;


}
