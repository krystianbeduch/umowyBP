package pl.bpwesley.TourOperator.reservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CountryDto {
    private Name name;
    private Idd idd;

    @Getter
    @Setter
    public static class Name {
        private String common;
    }

    @Getter
    @Setter
    public static class Idd {
        private String root;
        private List<String> suffixes;
    }
}