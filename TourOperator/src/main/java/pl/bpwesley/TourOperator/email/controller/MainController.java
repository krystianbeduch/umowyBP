package pl.bpwesley.TourOperator.email.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.bpwesley.TourOperator.email.service.EmailService;

@Controller
public class MainController {
    private final EmailService emailService;

    @Autowired
    public MainController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
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
