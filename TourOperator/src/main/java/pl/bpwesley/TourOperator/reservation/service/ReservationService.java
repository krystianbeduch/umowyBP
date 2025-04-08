package pl.bpwesley.TourOperator.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.reservation.entity.Participant;
import pl.bpwesley.TourOperator.reservation.entity.Reservation;
import pl.bpwesley.TourOperator.reservation.repository.ParticipantRepository;
import pl.bpwesley.TourOperator.reservation.repository.ReservationRepository;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ParticipantRepository participantRepository) {
        this.reservationRepository = reservationRepository;
        this.participantRepository = participantRepository;
    }

    public List<Reservation> getAllReservations() {
//        List<Participant> participants = participantRepository.findAll();
        return reservationRepository.findAll();
    }
}
