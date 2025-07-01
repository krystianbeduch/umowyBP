package pl.bpwesley.TourOperator.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.bpwesley.TourOperator.reservation.entity.tours.IndividualOneDayTour;

public interface IndividualOneDayTourRepository extends JpaRepository<IndividualOneDayTour, Long> {
}
