package pl.bpwesley.TourOperator.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.bpwesley.TourOperator.reservation.entity.Tour;

public interface TourRepository extends JpaRepository<Tour, Long> {
}
