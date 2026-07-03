package ge.appointmentservice.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;



@Entity
@Table(name = "clients")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    private static final int MALE_RETIREMENT_AGE = 65;
    private static final int FEMALE_RETIREMENT_AGE = 60;

    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String keycloakUserId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true)
    private String email;


    @Enumerated(EnumType.STRING)
    private Sex sex;


    @Column(nullable = false, unique = true)
    private String phoneNumber;


    @JsonIgnore
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Review> reviews;

    @JsonIgnore
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Appointment> appointments;



    private int computeAge(){
        LocalDate today = LocalDate.now();
        return Period.between(dateOfBirth, today).getYears();
    }




    public boolean retireeVerification(){

        return switch(sex){
                case MALE -> computeAge() >= MALE_RETIREMENT_AGE;
              case FEMALE -> computeAge() >= FEMALE_RETIREMENT_AGE;
          };
    }

}
