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
    private List<ParticipantDto> participantDtoList = new ArrayList<>();
    private Long tourId;
}