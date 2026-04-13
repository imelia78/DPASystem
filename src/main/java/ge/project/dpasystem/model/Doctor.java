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
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private Integer experience;

    @Column(nullable = false)
    private Integer age;


    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;


    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Review> reviews;


}
