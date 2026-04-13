package ge.project.dpasystem.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Setter
@Getter
@NoArgsConstructor
public class Review {
    // review should be matched with particular appointment!
    // Ability to write review should be provided only after verification
    // For excepting fake reviews

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Double rating;  //  Enum


    @Column(nullable = false)
    private String comment; // description


    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false,unique = true) // appointment_id should be unique to except redundancy
    private Appointment appointment;


    @ManyToOne
    @JoinColumn(name = "doctor_id",nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;


    @PrePersist
    private void onCreate(){
        createdAt = LocalDateTime.now();
    }

}
