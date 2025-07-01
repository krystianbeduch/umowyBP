package pl.bpwesley.TourOperator.reservation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PriceVariantDto {
    private Long id;
    private String name;
    private Integer price;
    private Integer ageFrom;
    private Integer ageTo;

    private LocalDate birthDateFrom;
    private LocalDate birthDateTo;

    public void calculateBirthDates(LocalDate tourStartDate) {
        if (ageFrom != null) {
            this.birthDateTo = tourStartDate.minusYears(ageFrom);
        }
        else {
            this.birthDateTo = null;
        }

        if (ageTo != null) {
            this.birthDateFrom = tourStartDate.minusYears(ageTo + 1).plusDays(1);
        }
        else {
            this.birthDateFrom = null;
        }
    }
}
