package ge.project.dpasystem.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

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

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private Integer experience; //сменить на  String professionalDescription?

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false,unique = true)
    private String email;


    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;


    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Review> reviews;


}
