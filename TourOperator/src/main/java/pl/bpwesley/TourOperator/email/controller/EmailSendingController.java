package pl.bpwesley.TourOperator.email.controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDto;
import pl.bpwesley.TourOperator.email.exception.EmailTemplateNotFoundException;
import pl.bpwesley.TourOperator.email.service.EmailSendingService;
import pl.bpwesley.TourOperator.email.service.EmailService;

import java.io.IOException;
import java.util.Optional;

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
        Optional<EmailTemplateDto> emailTemplateDtoOptional = emailService.getEmailTemplateById(id);

        // Zabezpieczenie przed brakiem szablonu
        if (emailTemplateDtoOptional.isEmpty()) {
            throw new EmailTemplateNotFoundException("BŁĄD PODCZAS WYSYŁANIA MAILA: nie istnieje szablon o id " + id);
        }
        // Zmiana z typu Optional
        EmailTemplateDto emailTemplate = emailTemplateDtoOptional.get();

        emailSendingService.sendEmail(emailTemplate,"beduch_krystian@o2.pl");

        redirectAttributes.addFlashAttribute("successMessage", "Email z szablonu \"" + emailTemplate.getTemplateName() + "\" wysłany");
        return "redirect:/email/home";
    }
}