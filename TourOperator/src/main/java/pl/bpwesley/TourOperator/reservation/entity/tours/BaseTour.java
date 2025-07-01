package pl.bpwesley.TourOperator.reservation.entity.tours;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.bpwesley.TourOperator.reservation.entity.Reservation;
import pl.bpwesley.TourOperator.reservation.entity.enums.TfgFee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class BaseTour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tourId;

    private String catalogId;
    private String title;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate finalPaymentDate;

    private int numberOfDaysOfAdvancePaymentAfterBooking;
    private int numberOfAvailableSeats;
    private int numberOfSeatsForMessageLastSeats;

//    @Enumerated(EnumType.STRING)
//    private TourType type;
    private String location;
    private String transport; // docelowo enum
    private String catering;
    private String insurance;
    private String notes;

    private Integer price;

    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "individualOneDayTour", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}
