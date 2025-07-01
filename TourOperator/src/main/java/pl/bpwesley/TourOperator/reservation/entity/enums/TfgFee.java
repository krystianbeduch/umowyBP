package pl.bpwesley.TourOperator.reservation.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TfgFee {
    CHARTER_EUROPE_AND_BEYOND_BEFORE("Czarter lotniczy (Europa i poza Europą) - przed", 15),
    CHARTER_EUROPE_AND_BEYOND_AFTER("Czarter lotniczy (Europa i poza Europą) - po", 8),

    OTHER_TRANSPORT_OUTSIDE_EUROPE_BEFORE("Inne transporty poza Europą - przed", 13),
    OTHER_TRANSPORT_OUTSIDE_EUROPE_AFTER("Inne transporty poza Europą - po", 7),

    NO_TRANSPORT_OUTSIDE_EUROPE_BEFORE("Imprezy poza Europą bez transportu - przed", 7),
    NO_TRANSPORT_OUTSIDE_EUROPE_AFTER("Imprezy poza Europą bez transportu - po", 4),

    EUROPE_OTHER_TRANSPORT_BEFORE("Europa - inne środki transportu niż czarter - przed", 10),
    EUROPE_OTHER_TRANSPORT_AFTER("Europa - inne środki transportu niż czarter - po", 5),

    EUROPE_NO_TRANSPORT_BEFORE("Europa - bez transportu - przed", 5),
    EUROPE_NO_TRANSPORT_AFTER("Europa - bez transportu - po", 3),

    POLAND_AND_NEIGHBOURS_BEFORE("Polska oraz kraje z lądową granicą z PL - przed", 2),
    POLAND_AND_NEIGHBOURS_AFTER("Polska oraz kraje z lądową granicą z PL - po", 0);

    private final String description;
    private final int amountInPln;

    @Override
    public String toString() {
        return description + " (" + amountInPln + " zł)";
    }
}

