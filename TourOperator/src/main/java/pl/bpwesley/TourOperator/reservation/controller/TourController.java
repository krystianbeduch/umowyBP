package pl.bpwesley.TourOperator.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.bpwesley.TourOperator.reservation.PriceVariantDto;
//import pl.bpwesley.TourOperator.reservation.dto.TourWithPriceVariantDto;
import pl.bpwesley.TourOperator.reservation.entity.enums.Transport;
import pl.bpwesley.TourOperator.reservation.entity.tours.IndividualOneDayTour;
import pl.bpwesley.TourOperator.reservation.service.IndividualOneDayTourService;
import pl.bpwesley.TourOperator.reservation.service.PriceVariantService;

import java.util.List;

@Controller
@RequestMapping("/tour")
public class TourController {
    private final IndividualOneDayTourService individualOneDayTourService;
    private final PriceVariantService priceVariantService;


//    public TourController(IndividualOneDayTourService individualOneDayTourService, PriceVariantService priceVariantService) {
//        this.individualOneDayTourService = individualOneDayTourService;
//        this.priceVariantService = priceVariantService;
//    }

    @Autowired
    public TourController(IndividualOneDayTourService individualOneDayTourService, PriceVariantService priceVariantService) {
        this.individualOneDayTourService = individualOneDayTourService;
        this.priceVariantService = priceVariantService;
    }

    @GetMapping("/individual-one-day/new")
    public String showNewTourForm(Model model) {
        model.addAttribute("pageTitle", "Dodawanie nowego wyjazdu");
        // TODO: skladek nie ma w wycieczkach 1-dniowych
        //model.addAttribute("tfgFeeValues", TfgFee.values());

        model.addAttribute("transportValues", Transport.values());
        model.addAttribute("tour", new IndividualOneDayTour());
        return "tour/individual-one-day/tour-form";
    }

    @GetMapping("/individual-one-day/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        IndividualOneDayTour tour = individualOneDayTourService.getTourById(id);
        if (tour == null) {
            model.addAttribute("errorMessage", "Nie znaleziono wycieczki");
            return  "redirect:/tour/home";
        }

        model.addAttribute("tour", tour);
        model.addAttribute("pageTitle", "Edycja wycieczki");
        model.addAttribute("transportValues", Transport.values());

        List<PriceVariantDto> priceVariantDtos = priceVariantService.getPriceVariantsDto(
                tour.getPriceVariants(), tour.getStartDate()
        );
        model.addAttribute("priceVariants", priceVariantDtos);
        return "tour/individual-one-day/tour-form";
    }

    @PostMapping("/individual-one-day/save")
    public String saveNewTour(@ModelAttribute("tour") IndividualOneDayTour tour) {
        individualOneDayTourService.saveNewTour(tour);
        return "redirect:/tour/home";
    }

    @GetMapping("/home")
    public String tourHome(Model model) {
        model.addAttribute("tours", individualOneDayTourService.getAllTours());
        model.addAttribute("pageTitle", "Wyjazdy");
        return "tour/home";
    }




//    public
}
