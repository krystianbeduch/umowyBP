package pl.bpwesley.TourOperator.reservation.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tours")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tourId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate finalPaymentDate;
    private int numberOfDaysOfAdvancePaymentAfterBooking;
    private int numberOfAvailableSeats;
    private int numberOfSeatsForMessageLastSeats;
    private String type;
    private String location;
    private String transport;
    private String hotel;
    private String catering;
    private String insurance;
    private String notesToTheAgreement;
    private String price;
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}
