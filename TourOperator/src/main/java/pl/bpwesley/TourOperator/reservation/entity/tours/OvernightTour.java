package pl.bpwesley.TourOperator.reservation.entity.tours;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import pl.bpwesley.TourOperator.reservation.entity.enums.TfgFee;

public class OvernightTour {
    private String hotel;
    @Enumerated(EnumType.STRING)
    private TfgFee tfgFee;
}
