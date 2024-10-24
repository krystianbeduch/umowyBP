package pl.bpwesley.TourOperator.email.controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDTO;
import pl.bpwesley.TourOperator.email.entity.EmailTemplateVariable;
import pl.bpwesley.TourOperator.email.exception.EmailTemplateNotFoundException;
import pl.bpwesley.TourOperator.email.service.EmailSendingService;
import pl.bpwesley.TourOperator.email.service.EmailService;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/email/send")
public class EmailSendingController {
    private final EmailSendingService emailSendingService;
    private final EmailService emailService;

    @Autowired
    public EmailSendingController(EmailSendingService emailSendingService, EmailService emailService) {
        this.emailSendingService = emailSendingService;
        this.emailService = emailService;
    }

    @GetMapping("/{id}")
    public String sendEmail(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) throws MessagingException, IOException {
        // Pobierz szablon email na podstawie ID
        Optional<EmailTemplateDTO> emailTemplateDtoOptional = emailService.getEmailTemplateById(id);

        // Zabezpieczenie przed brakiem szablonu
        if (emailTemplateDtoOptional.isEmpty()) {
            throw new EmailTemplateNotFoundException("BŁĄD PODCZAS WYSYŁANIA MAILA: nie istnieje szablon o id " + id);
        }
        EmailTemplateDTO emailTemplate = emailTemplateDtoOptional.get();
        String emailTemplateName = emailTemplate.getName();

        // Pobierz zmienne powiązane z szablonem z bazy danych
        List<EmailTemplateVariable> emailTemplateVariables = emailService.getEmailTemplateVariablesByTemplateId(emailTemplate.getEmailTemplateId());

        Map<String, Object> variables = new HashMap<>();
        List<String> attachments = new ArrayList<>();

        // Przypisz wartości zmiennych z bazy danych
//        for (EmailTemplateVariable emailTemplateVariable : emailTemplateVariables) {
//            String variableName = emailTemplateVariable.getVariable().getName();
////            String variableValue = getVariableValueFromDatabase(variableName); // Funkcja zwracająca wartość zmiennej z bazy danych
////            variables.put(variableName, variableValue);
//        }

        switch (emailTemplateName) {
            case "Potwierdzenie rezerwacji":
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

                // Dodaj załączniki
                Collections.addAll(attachments,
                        "Andrzejki-w-stylu-Country-3545i.pdf",
                        "OWU_BP_Wesley.pdf",
                        "OWU_PZU_NNW.pdf",
                        "Polityka_prywatnosci.pdf",
                        "Regulamin_serwisu.pdf",
                        "Umowa.pdf"
                );
                emailSendingService.sendEmail(
                        emailTemplate,
                        "beduch_krystian@o2.pl",
                        "Rezerwacja wycieczki " + variables.get("tour_name"),
                        emailTemplateVariables,
                        attachments
                );
                break;
            case "Potwierdzenie płatności zaliczki":
                variables.put("client_name", "Imie");
                variables.put("advance", "kwota zaliczki 200zl");
                variables.put("tour_name", "Nazwa wycieczki");
                variables.put("tour_id", "Id wycieczki");
                variables.put("tour_location", "Tour location");
                variables.put("remaining_ammount", "Pozostala kwota do zaplaty 200zl");
                variables.put("remaining_amount_deadline", "pozostaala kwota deadline");

                // Dodaj załączniki
                Collections.addAll(attachments,
                        "Umowa.pdf"
                );
                emailSendingService.sendEmail(
                        emailTemplate,
                        "beduch_krystian@o2.pl",
                        emailTemplateName + " za " + variables.get("tour_name") + " " + variables.get("tour_id"),
                        emailTemplateVariables,
                        attachments
                );
                break;
            case "Potwierdzenie płatności całości":
                variables.put("client_name", "Imie klienta");
                variables.put("tour_name", "Nazwa wycieczki");
                variables.put("tour_id", "Id wycieczki");
                variables.put("tour_location", "Tour location");
                variables.put("start_date", "dataod: 44");

                // Dodaj załączniki - brak

                emailSendingService.sendEmail(
                        emailTemplate,
                        "beduch_krystian@o2.pl",
                        emailTemplateName + " za " + variables.get("tour_name") + " " + variables.get("tour_id"),
                        emailTemplateVariables,
                        attachments
                );
                break;
            case "Przypomnienie o zbiórce na wycieczkę":
                variables.put("client_name", "Imie klienta");
                variables.put("start_date", "datazbiorki: 44");
                variables.put("tour_name", "Nazwa wycieczki");
                variables.put("tour_id", "Id wycieczki");
                variables.put("tour_location", "Tour location");
                variables.put("departure_time", "06:00");

                // Dodaj załączniki
                Collections.addAll(attachments,
                        "Umowa.pdf"
                );

                emailSendingService.sendEmail(
                        emailTemplate,
                        "beduch_krystian@o2.pl",
                        emailTemplateName + " " + variables.get("tour_name") + " " + variables.get("tour_id"),
                        emailTemplateVariables,
                        attachments
                );
                break;

        }
        redirectAttributes.addFlashAttribute("successMessage", "Email z szablonu o id " + emailTemplate.getEmailTemplateId() + " wysłany");
        return "redirect:/email/home";
    }
}