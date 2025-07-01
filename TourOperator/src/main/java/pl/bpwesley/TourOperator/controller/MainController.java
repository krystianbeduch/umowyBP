package pl.bpwesley.TourOperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.bpwesley.TourOperator.email.service.EmailService;
import pl.bpwesley.TourOperator.reservation.entity.Participant;
import pl.bpwesley.TourOperator.reservation.entity.Reservation;
import pl.bpwesley.TourOperator.reservation.service.ReservationService;
import pl.bpwesley.TourOperator.reservation.service.IndividualOneDayTourService;

import java.util.Comparator;
import java.util.List;

@Controller
public class MainController {
    private final EmailService emailService;
    private final IndividualOneDayTourService tourService;
    private final ReservationService reservationService;

    @Autowired
    public MainController(EmailService emailService, IndividualOneDayTourService individualOneDayTourService, ReservationService reservationService) {
        this.emailService = emailService;
        this.tourService = individualOneDayTourService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }


//    @GetMapping("/tour/home")
//    public String tourHome(Model model) {
//        model.addAttribute("tours", tourService.getAllTours());
//        model.addAttribute("pageTitle", "Wyjazdy");
//        return "tour/home";
//    }

    @GetMapping("/reservation/home")
    public String reservationHome(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        for (Reservation reservation : reservations) {
            reservation.getParticipants().sort(Comparator.comparing(Participant::isMainParticipant).reversed()
                    .thenComparing(Participant::getFirstName)
                    .thenComparing(Participant::getLastName));
        }

        model.addAttribute("reservations", reservations);
        model.addAttribute("pageTitle", "Rezerwacje");
        return "reservation/home";
    }

    @GetMapping("/email/home")
    public String emailHome(Model model) {
        // Zaladuj liste szablonow
        model.addAttribute("emailTemplates", emailService.getEmailTemplateList());
        return "email/home";
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin/home";
    }

}
