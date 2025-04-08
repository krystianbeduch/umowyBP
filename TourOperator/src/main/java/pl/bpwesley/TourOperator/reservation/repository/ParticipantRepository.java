package pl.bpwesley.TourOperator.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bpwesley.TourOperator.reservation.entity.Participant;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
//    @Query("SELECT p FROM Participant p ORDER BY p.reservation.reservationId, " +
//            "CASE WHEN p.mainParticipant = true THEN 0 ELSE 1 END, " +
//            "p.firstName ASC, p.lastName ASC")
//    List<Participant> findAllReservationWithParticipants();
}
