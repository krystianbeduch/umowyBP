package pl.bpwesley.TourOperator.reservation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.bpwesley.TourOperator.reservation.PriceVariantDto;
import pl.bpwesley.TourOperator.reservation.entity.tours.PriceVariant;

@Mapper
public interface PriceVariantMapper {
    PriceVariantMapper INSTANCE = Mappers.getMapper(PriceVariantMapper.class);

    PriceVariantDto priceVariantToDto(PriceVariant priceVariant);
    PriceVariant dtoToPriceVariant(PriceVariantDto priceVariantDto);
}