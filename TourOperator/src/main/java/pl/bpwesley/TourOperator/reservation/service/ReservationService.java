package pl.bpwesley.TourOperator.reservation.service;

import jakarta.transaction.Transactional;
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
    private final PriceVariantService priceVariantService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ParticipantRepository participantRepository, PriceVariantService priceVariantService) {
        this.reservationRepository = reservationRepository;
        this.participantRepository = participantRepository;
        this.priceVariantService = priceVariantService;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void saveReservation(Reservation reservation, Long tourId) {
        if (reservation != null && tourId != null) {
            for (Participant p : reservation.getParticipants()) {
                p.setPrice(priceVariantService.calculatePrice(tourId, p.getBirthDate()));
                p.setReservation(reservation);
            }
            reservationRepository.save(reservation);
        }
    }
}
