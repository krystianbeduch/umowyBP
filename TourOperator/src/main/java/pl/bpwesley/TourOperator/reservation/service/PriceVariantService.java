package pl.bpwesley.TourOperator.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.reservation.PriceVariantDto;
import pl.bpwesley.TourOperator.reservation.entity.tours.IndividualOneDayTour;
import pl.bpwesley.TourOperator.reservation.entity.tours.PriceVariant;
import pl.bpwesley.TourOperator.reservation.exception.PriceCalculationException;
import pl.bpwesley.TourOperator.reservation.mapper.PriceVariantMapper;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PriceVariantService {
    private final IndividualOneDayTourService tourService;

    @Autowired
    public PriceVariantService(IndividualOneDayTourService tourService) {
        this.tourService = tourService;
    }

    public List<PriceVariantDto> getPriceVariantsDto(List<PriceVariant> variants, LocalDate tourStartDate) {
        return variants.stream()
                .map(variant -> {
                    PriceVariantDto dto = PriceVariantMapper.INSTANCE.priceVariantToDto(variant);
                    dto.calculateBirthDates(tourStartDate);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public int calculatePrice(Long tourId, LocalDate birthDate) {
        IndividualOneDayTour tour = tourService.getTourById(tourId);

        if (birthDate == null || tour == null || tour.getStartDate() == null) {
            throw new PriceCalculationException("Brak danych do obliczenia ceny");
        }

        int age = Period.between(birthDate, tour.getStartDate()).getYears();
        List<PriceVariant> variantList = Optional.ofNullable(tour.getPriceVariants()).orElse(Collections.emptyList());
        Optional<Integer> variantPrice = variantList.stream()
                .filter(pv -> {
                    Integer from = pv.getAgeFrom();
                    Integer to = pv.getAgeTo();
                    boolean meetsFrom = from == null || age >= from;
                    boolean meetsTo = to == null || age <= to;
                    return meetsFrom && meetsTo;
                })
                .map(PriceVariant::getPrice)
                .findFirst();

        return variantPrice.orElse(tour.getPrice());
    }
}