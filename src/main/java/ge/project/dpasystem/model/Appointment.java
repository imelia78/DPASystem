package ge.project.dpasystem.model;


import ge.project.dpasystem.dto.AddressDto;
import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "appointment_date_time"})
        )
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "appointment_date_time")
    private LocalDateTime appointmentDateTime;

    private Integer duration;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id",nullable = false)
    private Client client;


    @Enumerated(value = EnumType.STRING)
    private AppointmentStatus appointmentStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Review review;

    //private Discount discount;

    @Embedded
    private Address appointmentAddress;

}
