package pl.bpwesley.TourOperator.email.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.bpwesley.TourOperator.email.service.EmailService;

@ControllerAdvice
public class EmailTemplateExceptionHandler {
    private final EmailService emailService;

    @Autowired
    public EmailTemplateExceptionHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @ExceptionHandler(EmailTemplateNotFoundException.class)
    public String handleEmailTemplateNotFoundException(EmailTemplateNotFoundException ex, RedirectAttributes redirectAttributes) {
        // Wyswietl blad
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        // Przekieruj na stronę email/home
        return "redirect:/email/home";
    }
}
