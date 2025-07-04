package pl.bpwesley.TourOperator.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PrefixCountryDto {
    private String commonName;
    private String phoneCode;
}