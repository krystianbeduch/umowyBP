package pl.bpwesley.TourOperator.email.controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.bpwesley.TourOperator.email.service.EmailSendingService;
import pl.bpwesley.TourOperator.email.service.EmailService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/email/send")
public class EmailSendingController {
    private final EmailSendingService emailSendingService;

    @Autowired
    public EmailSendingController(EmailSendingService emailSendingService) {
        this.emailSendingService = emailSendingService;
    }

    @ResponseBody
    @GetMapping("/reservation-confirmation")
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

        emailSendingService.sendEmailWithReservationConfirmation(
                "beduch_krystian@o2.pl",
                "Rezerwacja wycieczki ",
                variables
        );
        return "Potwierdzenie rezerwacji wyslane";
    }

    @ResponseBody
    @GetMapping("/advance-payment-confirmation")
    public String sendEmailWithAdvancePaymentConfirmation() throws MessagingException, UnsupportedEncodingException {
//        Docelowo DB
        Map<String, Object> variables = new HashMap<>();
        variables.put("client_name", "Imie");
        variables.put("advance", "kwota zaliczki 200zl");
        variables.put("tour_name", "Nazwa wycieczki");
        variables.put("tour_id", "Id wycieczki");
        variables.put("tour_location", "Tour location");
        variables.put("remaining_ammount", "Pozostala kwota do zaplaty 200zl");
        variables.put("remaining_amount_deadline", "pozostaala kwota deadline");

        emailSendingService.sendEmailWithAdvancePaymentConfirmation(
                "beduch_krystian@o2.pl",
                "Potwierdzenie płatności zaliczki za ",
                variables
        );
        return "Potwierdzenie płatności zaliczki wyslane";
    }



}
