package pl.bpwesley.TourOperator.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.bpwesley.TourOperator.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
