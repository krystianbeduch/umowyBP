package pl.bpwesley.TourOperator.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.bpwesley.TourOperator.groupsAdminPanel.exception.ClientNotFoundException;
import pl.bpwesley.TourOperator.reservation.PriceVariantDto;
//import pl.bpwesley.TourOperator.reservation.dto.TourWithPriceVariantDto;
import pl.bpwesley.TourOperator.reservation.entity.enums.Transport;
import pl.bpwesley.TourOperator.reservation.entity.tours.IndividualOneDayTour;
import pl.bpwesley.TourOperator.reservation.exception.TourNotFoundException;
import pl.bpwesley.TourOperator.reservation.service.IndividualOneDayTourService;
import pl.bpwesley.TourOperator.reservation.service.PriceVariantService;

import java.util.List;
import java.util.Map;

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
        // TODO: skladek nie ma w wycieczkach 1-dniowych
        //model.addAttribute("tfgFeeValues", TfgFee.values());

        model.addAttribute("tour", new IndividualOneDayTour());
        model.addAttribute("transportValues", Transport.values());
        model.addAttribute("pageTitle", "Dodawanie nowego wyjazdu");
//        model.addAttribute("editMode", false);
        return "tour/individual-one-day/tour-form";
    }

    @PostMapping("/individual-one-day/save")
    public String saveNewTour(@ModelAttribute("tour") IndividualOneDayTour tour, RedirectAttributes redirectAttributes) {
        individualOneDayTourService.saveNewTour(tour);
        redirectAttributes.addFlashAttribute("successMessage", "Dodano wycieczke");
        return "redirect:/tour/home";
    }

    @GetMapping("/individual-one-day/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        IndividualOneDayTour tour = individualOneDayTourService.getTourById(id);
        if (tour == null) {
            redirectAttributes.addAttribute("errorMessage", "Nie znaleziono wycieczki");
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

    @PostMapping("/individual-one-day/update")
    public String updateTour(@ModelAttribute("tour") IndividualOneDayTour tour, RedirectAttributes redirectAttributes) {
        try {
            individualOneDayTourService.updateTour(tour);
            redirectAttributes.addFlashAttribute("successMessage", "Zaktualizowano wycieczkę o ID: " + tour.getTourId());
        }
        catch (TourNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/tour/home";
    }

    @DeleteMapping("/individual-one-day/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteTour(@PathVariable Long id) {
        try {
            individualOneDayTourService.deleteTourById(id);
            return ResponseEntity.ok()
                    .body(Map.of("message", "Usunięto wycieczkę o ID: " + id));

//            redirectAttributes.addFlashAttribute("successMessage", "Usunięto wycieczkę o ID: " + id);
        }
        catch (TourNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", e.getMessage()));
//            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Wystąpił błąd podczas usuwania wycieczki"));
        }
//        return "redirect:/tour/home";
    }

    @GetMapping("/home")
    public String tourHome(Model model) {
        model.addAttribute("tours", individualOneDayTourService.getAllTours());
        model.addAttribute("pageTitle", "Wyjazdy");
        return "tour/home";
    }
}
