package pl.bpwesley.TourOperator.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class ParticipantDto {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phonePrefix;
    private String phone;
    private int price;
}