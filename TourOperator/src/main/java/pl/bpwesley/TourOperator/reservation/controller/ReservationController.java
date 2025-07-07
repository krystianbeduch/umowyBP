package pl.bpwesley.TourOperator.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.bpwesley.TourOperator.reservation.dto.ParticipantDto;
import pl.bpwesley.TourOperator.reservation.dto.ReservationFormDto;
import pl.bpwesley.TourOperator.reservation.entity.Participant;
import pl.bpwesley.TourOperator.reservation.entity.Reservation;
import pl.bpwesley.TourOperator.reservation.entity.tours.IndividualOneDayTour;
import pl.bpwesley.TourOperator.reservation.mapper.ReservationMapper;
import pl.bpwesley.TourOperator.reservation.service.CountryService;
import pl.bpwesley.TourOperator.reservation.service.IndividualOneDayTourService;
import pl.bpwesley.TourOperator.reservation.service.ReservationService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final IndividualOneDayTourService individualOneDayTourService;
    private final CountryService countryService;
    private final ReservationService reservationService;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    public ReservationController(IndividualOneDayTourService individualOneDayTourService, CountryService countryService, ReservationService reservationService) {
        this.individualOneDayTourService = individualOneDayTourService;
        this.countryService = countryService;
        this.reservationService = reservationService;
    }

    @GetMapping("/{tourId}")
    public String tourHome(@PathVariable Long tourId, Model model) {
        IndividualOneDayTour tour = individualOneDayTourService.getTourById(tourId);
        model.addAttribute("tour", tour);

        ReservationFormDto reservationFormDto = new ReservationFormDto();
        reservationFormDto.setTourId(tourId);
        ParticipantDto reserver = new ParticipantDto();
        reservationFormDto.getParticipants().add(reserver);
        model.addAttribute("reservationForm", reservationFormDto);

        model.addAttribute("pageTitle", "Rezerwacja wycieczki");
        model.addAttribute("countries", countryService.getCountries());
        return "tour/individual-one-day/reservation-form";
    }

    @PostMapping("/send")
    @ResponseBody
    public String handleReservation(@ModelAttribute ReservationFormDto reservationFormDto, Model model) {
        IndividualOneDayTour tour = individualOneDayTourService.getTourById(reservationFormDto.getTourId());
        Reservation reservation = reservationMapper.mapToReservation(reservationFormDto, tour);

        reservationService.saveReservation(reservation, reservationFormDto.getTourId());

//        model.addAttribute("success", true);

        return "dzięki za rezerwacje";
    }
}
