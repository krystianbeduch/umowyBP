package pl.bpwesley.TourOperator.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private int price;

    // TODO: platnosci
    private boolean confirmed;
    private boolean advancePaymentPaid;
    private boolean totalPaid;
    //

    private String comment; // TODO: dodawanie notatki przez panel
    private boolean mainParticipant; // ? byc moze nie potrzebne

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
