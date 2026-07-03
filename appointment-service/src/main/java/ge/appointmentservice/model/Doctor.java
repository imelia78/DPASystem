package ge.appointmentservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @Column(nullable = true)
    private String adminComment;


    @Column(nullable = false)
    private String professionalDescription;


    @Column(nullable = false, unique = true)
    private String stateCertificateNumber;


    @Column(nullable = false)
    @Builder.Default
    private Double averageRating = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Integer reviewsCount = 0;


    @Enumerated(EnumType.STRING)
    private  VerificationStatus verificationStatus;


    @JsonIgnore
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Appointment> appointments;


    @JsonIgnore
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Review> reviews;


}
