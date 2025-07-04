package pl.bpwesley.TourOperator.reservation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.bpwesley.TourOperator.reservation.dto.ReservationFormDto;
import pl.bpwesley.TourOperator.reservation.entity.tours.BaseTour;
import pl.bpwesley.TourOperator.reservation.entity.tours.IndividualOneDayTour;
import pl.bpwesley.TourOperator.reservation.service.CountryService;
import pl.bpwesley.TourOperator.reservation.service.IndividualOneDayTourService;

import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final IndividualOneDayTourService individualOneDayTourService;
    private final CountryService countryService;

    @Autowired
    public ReservationController(IndividualOneDayTourService individualOneDayTourService, CountryService countryService) {
        this.individualOneDayTourService = individualOneDayTourService;
        this.countryService = countryService;
    }

    @GetMapping("/{tourId}")
    public String tourHome(@PathVariable Long tourId, Model model) throws JsonProcessingException {
        IndividualOneDayTour tour = individualOneDayTourService.getTourById(tourId);
        model.addAttribute("tour", tour);
        model.addAttribute("pageTitle", "Rezerwacja wycieczki");
        model.addAttribute("reservationForm", new ReservationFormDto());
        model.addAttribute("countries", countryService.getCountries());
        return "tour/individual-one-day/reservation-form";
    }
}
