package pl.bpwesley.TourOperator.reservation.service;

import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.reservation.PriceVariantDto;
import pl.bpwesley.TourOperator.reservation.entity.tours.PriceVariant;
import pl.bpwesley.TourOperator.reservation.mapper.PriceVariantMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceVariantService {
    public PriceVariantService() {}

    public List<PriceVariantDto> getPriceVariantsDto(List<PriceVariant> variants, LocalDate tourStartDate) {
        return variants.stream()
                .map(variant -> {
                    PriceVariantDto dto = PriceVariantMapper.INSTANCE.priceVariantToDto(variant);
                    dto.calculateBirthDates(tourStartDate);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
