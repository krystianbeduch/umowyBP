package pl.bpwesley.TourOperator.email.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDTO;
import pl.bpwesley.TourOperator.email.entity.Attachment;
import pl.bpwesley.TourOperator.email.exception.EmailTemplateNotFoundException;
import pl.bpwesley.TourOperator.email.service.EmailService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @Transactional
    public String showEditEmailTemplatePage(
            @PathVariable("id") Long emailTemplateId,
            Model model) {
        Optional<EmailTemplateDTO> emailTemplateDtoOptional = emailService.getEmailTemplateById(emailTemplateId);

        if (emailTemplateDtoOptional.isPresent()) {
            // Pobieramy zawartosc szablonu na podstawie przekazanego ID
            EmailTemplateDTO emailTemplateDTO = emailTemplateDtoOptional.get();
            model.addAttribute("emailTemplateId", emailTemplateDTO.getEmailTemplateId());
            model.addAttribute("emailTemplateContent", emailTemplateDTO.getContent());
            model.addAttribute("emailTemplateName", emailTemplateDTO.getTemplateName());
            model.addAttribute("attachments", emailTemplateDTO.getAttachments());
        }
        else {
            throw new EmailTemplateNotFoundException("BŁĄD SZABLONU: szablon o id " + emailTemplateId + " nie istnieje");
        }

        return "email/edit_template";
    }

    @PutMapping("/edit-template")
    public String updateEmailTemplate(@RequestParam("templateId") Long templateId,
                                      @RequestParam("templateName") String templateName,
                                      @RequestParam("content") String content,
                                      @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments,
                                      RedirectAttributes redirectAttributes) throws IOException {

        EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();
        emailTemplateDTO.setEmailTemplateId(templateId);
        emailTemplateDTO.setTemplateName(templateName);
        emailTemplateDTO.setContent(content);
        emailTemplateDTO.setUpdateDate(LocalDateTime.now());

        // Przetwarzanie załączników i dodawanie ich do DTO
        if (attachments != null && !attachments.isEmpty()) {
//            List<AttachmentDTO> attachmentDTOs = new ArrayList<>();
            List<Attachment> attachmentDTOs = new ArrayList<>();
            for (MultipartFile file : attachments) {
                if (!file.isEmpty()) {
//                AttachmentDTO attachmentDTO = new AttachmentDTO();
                    Attachment attachmentDTO = new Attachment();
                    attachmentDTO.setFilename(file.getOriginalFilename());
                    attachmentDTO.setFileData(file.getBytes()); // Możliwe przekształcenie danych na byte[]
                    attachmentDTOs.add(attachmentDTO);
                }
            }

            emailTemplateDTO.setAttachments(attachmentDTOs);
        }

        emailService.updateEmailTemplate(emailTemplateDTO);

        redirectAttributes.addFlashAttribute("successMessage", "Szablon o id " + emailTemplateDTO.getEmailTemplateId() + " zaktualizowany");
        return "redirect:/email/home";
    }
}