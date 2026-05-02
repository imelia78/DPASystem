package ge.project.dpasystem.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "doctors")
public class Doctor {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column(nullable = false)
    String specialization;

    @Column(nullable = false)
    Integer experience;

    @Column(nullable = false)
    Integer age;


    @OneToMany(mappedBy = "doctor")
    List<Appointment> appointments;


    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    List<Review> reviews;


}
