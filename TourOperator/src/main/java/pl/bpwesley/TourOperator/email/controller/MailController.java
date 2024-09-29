package pl.bpwesley.TourOperator.email.controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.bpwesley.TourOperator.email.service.EmailService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/mail")
public class MailController {

    private final EmailService emailService;

    @Autowired
    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @ResponseBody
    @GetMapping("/send")
    public String sendEmailWithReservationConfirmation() throws MessagingException, IOException {
        // Dane do szablonu mailowego (docelowo DB)
        Map<String, Object> variables = new HashMap<>();
        variables.put("client_name", "Imie");
        variables.put("tour_name", "Wycieczka o nazwie");
        variables.put("start_date", "data_od");
        variables.put("end_date", "data_do");
        variables.put("advance", "zaliczka");
        variables.put("advance_deadline", "zaliczka termin");
        variables.put("remaining_amount", "pozostala kwota");
        variables.put("remaining_amount_deadline", "pozostaala kwota deadline");
        variables.put("departure_time", "06:00");
        variables.put("arrival_time", "21:00");

        emailService.sendEmailWithReservationConfirmation(
                "beduch_krystian@o2.pl",
                "Rezerwacja wycieczki ",
                variables
        );
//        emailService.sendSimpleMessage(
//                "beduch_krystian@o2.pl",
//                "Test",
//                "cos tam"
//        );
        return "mail_sent";
    }
}