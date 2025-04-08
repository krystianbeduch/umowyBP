package pl.bpwesley.TourOperator.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "main_participant_id")
    private Participant mainParticipant;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<Participant> participants;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

//    private boolean confirmed;
//    private boolean advancePaymentPaid;
//    private boolean totalPaid;
//    private String comment;

    @Lob
    private String pdfAgreement;
}
