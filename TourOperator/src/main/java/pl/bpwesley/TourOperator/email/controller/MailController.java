package pl.bpwesley.TourOperator.email.controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;
import pl.bpwesley.TourOperator.email.service.EmailService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/email")
public class MailController {

    private final EmailService emailService;

    @Autowired
    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/edit-template/{id}")
    public String showEditEmailTemplatePage(
            @PathVariable("id") Long templateId,
            Model model) {
        // Pobieramy zawartosc szablonu na podstawie przekazanego ID

        model.addAttribute("emailTemplateContent", emailService.getEmailTemplateContent(templateId));        model.addAttribute("emailTemplateId", templateId);
        model.addAttribute("emailTemplateName", emailService.getEmailTemplateName(templateId));
        return "email/edit_template";
    }

    @PutMapping("/edit-template")
    @ResponseBody
    public String updateEmailTemplate(@RequestParam("templateId") Long templateId,
                                      @RequestParam("name") String name,
                                      @RequestParam("content") String content) {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setEmailTemplateId(templateId);
        emailTemplate.setName(name);
        emailTemplate.setContent(content);
        emailTemplate.setUpdateDate(LocalDateTime.now());

        emailService.updateEmailTemplateContent(emailTemplate);

        return "Zaktualizwaono maila";
    }
}