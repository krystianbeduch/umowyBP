package pl.bpwesley.TourOperator.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.bpwesley.TourOperator.email.dto.AttachmentDto;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDto;
import pl.bpwesley.TourOperator.email.exception.EmailTemplateNotFoundException;
import pl.bpwesley.TourOperator.email.service.AttachmentService;
import pl.bpwesley.TourOperator.email.service.EmailService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;
    private final AttachmentService attachmentService;

    @Autowired
    public EmailController(EmailService emailService, AttachmentService attachmentService) {
        this.emailService = emailService;
        this.attachmentService = attachmentService;
    }

    @GetMapping("/edit-template/{id}")
    public String showEditEmailTemplatePage(
            @PathVariable("id") Long emailTemplateId,
            Model model) {
        Optional<EmailTemplateDto> emailTemplateDtoOptional = emailService.getEmailTemplateById(emailTemplateId);

        if (emailTemplateDtoOptional.isPresent()) {
            // Pobieramy zawartosc szablonu na podstawie przekazanego ID
            EmailTemplateDto emailTemplateDTO = emailTemplateDtoOptional.get();
            model.addAttribute("emailTemplateId", emailTemplateDTO.getEmailTemplateId());
            model.addAttribute("emailTemplateSubject", emailTemplateDTO.getTemplateSubject());
            model.addAttribute("emailTemplateContent", emailTemplateDTO.getContent());
            model.addAttribute("emailTemplateName", emailTemplateDTO.getTemplateName());
            model.addAttribute("attachments", emailTemplateDTO.getAttachmentDtos());
        }
        else {
            throw new EmailTemplateNotFoundException("BŁĄD SZABLONU: szablon o id " + emailTemplateId + " nie istnieje");
        }

        return "email/edit_template";
    }

    @PutMapping("/edit-template")
    public String updateEmailTemplate(@RequestParam("templateId") Long templateId,
                                      @RequestParam("templateName") String templateName,
                                      @RequestParam("templateSubject") String templateSubject,
                                      @RequestParam("content") String content,
                                      @RequestParam(value = "attachments", required = false) List<MultipartFile> newAttachments,
                                      @RequestParam(value = "existingAttachments", required = false) List<Long> existingAttachmentIds,
                                      RedirectAttributes redirectAttributes) throws IOException {

        EmailTemplateDto emailTemplateDto = new EmailTemplateDto();
        emailTemplateDto.setEmailTemplateId(templateId);
        emailTemplateDto.setTemplateName(templateName);
        emailTemplateDto.setTemplateSubject(templateSubject);
        emailTemplateDto.setContent(content);
        emailTemplateDto.setUpdateDate(LocalDateTime.now());

        List<AttachmentDto> attachmentDtos = attachmentService.processAttachments(newAttachments);
        emailTemplateDto.setAttachmentDtos(attachmentDtos);
        emailService.updateEmailTemplate(emailTemplateDto);

        redirectAttributes.addFlashAttribute("successMessage", "Szablon \"" + emailTemplateDto.getTemplateName() + "\" zaktualizowany");
        return "redirect:/email/home";
    }
}