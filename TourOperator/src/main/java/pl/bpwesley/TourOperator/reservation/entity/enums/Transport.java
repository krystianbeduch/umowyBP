package pl.bpwesley.TourOperator.reservation.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Transport {
    COACH("Autokar"),
    PLANE("Samolot"),
    CHARTER_FLIGHT("Samolot czarterowy"),
    COACH_AND_FERRY("Autokar + prom"),
    NO_TRANSPORT("Brak transportu");

    private final String displayName;
}
