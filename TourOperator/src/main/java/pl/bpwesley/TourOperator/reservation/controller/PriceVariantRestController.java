package pl.bpwesley.TourOperator.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.bpwesley.TourOperator.reservation.exception.PriceCalculationException;
import pl.bpwesley.TourOperator.reservation.service.IndividualOneDayTourService;
import pl.bpwesley.TourOperator.reservation.service.PriceVariantService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/tour")
public class PriceVariantRestController {
//    private final IndividualOneDayTourService tourService;
    private final PriceVariantService priceVariantService;

    @Autowired
    public PriceVariantRestController(IndividualOneDayTourService tourService, PriceVariantService priceVariantService) {
//        this.tourService = tourService;
        this.priceVariantService = priceVariantService;
    }

    @GetMapping("/price")
    public ResponseEntity<Integer> getPrice(
            @RequestParam("tourId") Long tourId,
            @RequestParam("birthDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate
    ) {
        try {
            int price = priceVariantService.calculatePrice(tourId, birthDate);
            return ResponseEntity.ok(price);
        }
        catch (PriceCalculationException e) {
            return  ResponseEntity.badRequest().build();
        }
    }
}