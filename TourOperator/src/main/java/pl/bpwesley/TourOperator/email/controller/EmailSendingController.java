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

        List<String> attachments = new ArrayList<>();
//        List<String> attachmentUrls = new ArrayList<>(); // Lista załącznikow z URL

        switch (emailTemplateName) {
            case "Potwierdzenie rezerwacji":
                // Dodaj załączniki
                Collections.addAll(attachments,
                        "Andrzejki-w-stylu-Country-3545i.pdf",
                        "OWU_BP_Wesley.pdf",
                        "OWU_PZU_NNW.pdf",
                        "Polityka_prywatnosci.pdf",
                        "Regulamin_serwisu.pdf",
                        "Umowa.pdf"
                );
//                Collections.addAll(attachmentUrls,
//                        "https://bpwesley.pl/images/dokumenty/polityka_prywatnosci_RODO.pdf",
//                        "https://bpwesley.pl/images/dokumenty/regulamin_serwisu_BP_Wesley.pdf",
//                        "https://bpwesley.pl/images/dokumenty/OWU_BP_Wesley_2023.pdf",
//                        "https://"
//                );
//
                emailSendingService.sendEmail(
                        emailTemplate,
                        "beduch_krystian@o2.pl",
                        "Rezerwacja wycieczki ",
                        emailTemplateVariables,
                        attachments
                );
                break;
            case "Potwierdzenie płatności zaliczki":
                // Dodaj załączniki
                Collections.addAll(attachments,
                        "Umowa.pdf"
                );
                emailSendingService.sendEmail(
                        emailTemplate,
                        "beduch_krystian@o2.pl",
                        emailTemplateName + " za ",
                        emailTemplateVariables,
                        attachments
                );
                break;
            case "Potwierdzenie płatności całości":
                // Dodaj załączniki - brak

                emailSendingService.sendEmail(
                        emailTemplate,
                        "beduch_krystian@o2.pl",
                        emailTemplateName + " za ",
                        emailTemplateVariables,
                        attachments
                );
                break;
            case "Przypomnienie o zbiórce na wycieczkę":
                // Dodaj załączniki
                Collections.addAll(attachments,
                        "Umowa.pdf"
                );

                emailSendingService.sendEmail(
                        emailTemplate,
                        "beduch_krystian@o2.pl",
                        emailTemplateName + " ",
                        emailTemplateVariables,
                        attachments
                );
                break;

        }
        redirectAttributes.addFlashAttribute("successMessage", "Email z szablonu o id " + emailTemplate.getEmailTemplateId() + " wysłany");
        return "redirect:/email/home";
    }
}