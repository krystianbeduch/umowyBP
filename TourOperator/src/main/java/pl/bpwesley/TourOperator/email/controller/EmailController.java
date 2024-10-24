package pl.bpwesley.TourOperator.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDTO;
import pl.bpwesley.TourOperator.email.exception.EmailTemplateNotFoundException;
import pl.bpwesley.TourOperator.email.service.EmailService;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/edit-template/{id}")
    public String showEditEmailTemplatePage(
            @PathVariable("id") Long emailTemplateId,
            Model model) {
        Optional<EmailTemplateDTO> emailTemplateDtoOptional = emailService.getEmailTemplateById(emailTemplateId);

        if (emailTemplateDtoOptional.isPresent()) {
            // Pobieramy zawartosc szablonu na podstawie przekazanego ID
            EmailTemplateDTO emailTemplateDTO = emailTemplateDtoOptional.get();
            model.addAttribute("emailTemplateId", emailTemplateDTO.getEmailTemplateId());
            model.addAttribute("emailTemplateContent", emailTemplateDTO.getContent());
            model.addAttribute("emailTemplateName", emailTemplateDTO.getName());
        }
        else {
            throw new EmailTemplateNotFoundException("Szablon o id " + emailTemplateId + " nnie istnieje");
        }

        return "email/edit_template";
    }

    @PutMapping("/edit-template")
    public String updateEmailTemplate(@RequestParam("templateId") Long templateId,
                                      @RequestParam("name") String name,
                                      @RequestParam("content") String content,
                                      RedirectAttributes redirectAttributes) {

        EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();
        emailTemplateDTO.setEmailTemplateId(templateId);
        emailTemplateDTO.setName(name);
        emailTemplateDTO.setContent(content);
        emailTemplateDTO.setUpdateDate(LocalDateTime.now());

        emailService.updateEmailTemplate(emailTemplateDTO);

        redirectAttributes.addFlashAttribute("message", "Szablon o ID " + emailTemplateDTO.getEmailTemplateId() + " zaktualizowany");
        return "redirect:/email/home";
    }
}