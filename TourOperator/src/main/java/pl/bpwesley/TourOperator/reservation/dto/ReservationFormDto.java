package pl.bpwesley.TourOperator.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ReservationFormDto {
    private Long tourId;
    private boolean reserverNotParticipating;
    private List<ParticipantDto> participants = new ArrayList<>();
}