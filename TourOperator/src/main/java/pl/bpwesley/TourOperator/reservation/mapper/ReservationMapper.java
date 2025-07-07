package pl.bpwesley.TourOperator.reservation.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import pl.bpwesley.TourOperator.reservation.dto.ReservationFormDto;
import pl.bpwesley.TourOperator.reservation.entity.Participant;
import pl.bpwesley.TourOperator.reservation.entity.Reservation;
import pl.bpwesley.TourOperator.reservation.entity.tours.IndividualOneDayTour;

@Mapper(componentModel = "spring", uses = { ParticipantMapper.class })
@Component
public abstract class ReservationMapper {

    @Mapping(target = "individualOneDayTour", source = "tour")
    public abstract Reservation mapToReservation(ReservationFormDto dto, IndividualOneDayTour tour);

    @AfterMapping
    protected void linkParticipants(@MappingTarget Reservation reservation) {
        if (reservation.getParticipants() != null && !reservation.getParticipants().isEmpty()) {
            for (Participant participant : reservation.getParticipants()) {
                participant.setReservation(reservation); // FK
            }
            reservation.setMainParticipant(reservation.getParticipants().getFirst()); // 1:1
        }
    }
}