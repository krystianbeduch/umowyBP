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
import pl.bpwesley.TourOperator.email.repository.AttachmentRepository;
import pl.bpwesley.TourOperator.email.service.AttachmentService;
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
    private final AttachmentRepository attachmentRepository;
    private final AttachmentService attachmentService;

    @Autowired
    public EmailController(EmailService emailService, AttachmentRepository attachmentRepository, AttachmentService attachmentService) {
        this.emailService = emailService;
        this.attachmentRepository = attachmentRepository;
        this.attachmentService = attachmentService;
    }

    @GetMapping("/edit-template/{id}")
//    @Transactional
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
                                      @RequestParam(value = "attachments", required = false) List<MultipartFile> newAttachments,
                                      @RequestParam(value = "existingAttachments", required = false) List<Long> existingAttachmentIds,
                                      RedirectAttributes redirectAttributes) throws IOException {

        EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();
        emailTemplateDTO.setEmailTemplateId(templateId);
        emailTemplateDTO.setTemplateName(templateName);
        emailTemplateDTO.setContent(content);
        emailTemplateDTO.setUpdateDate(LocalDateTime.now());

        List<Attachment> attachmentDTOs = attachmentService.processAttachments(newAttachments);
//        emailTemplateDTO.setAttachments(attachmentDTOs);

//        // Dodawanie istniejacych zalacznikow
//        if (existingAttachmentIds != null && !existingAttachmentIds.isEmpty()) {
//            for (Long attachmentId : existingAttachmentIds) {
//                Attachment attachment = attachmentService.getAttachmentById(attachmentId);
//                if (attachment != null) {
//                    attachmentDTOs.add(attachment);
//                }
//            }
//        }

        // Przetwarzanie nowych zalacznikow
//        if (newAttachments != null && !newAttachments.isEmpty()) {
////      dto toDo      List<AttachmentDTO> attachmentDTOs = new ArrayList<>();
//            for (MultipartFile file : newAttachments) {
//                if (!file.isEmpty()) {
////    dto toDo            AttachmentDTO attachmentDTO = new AttachmentDTO();
//                    Attachment attachmentDTO = new Attachment();
//                    attachmentDTO.setFilename(file.getOriginalFilename());
//                    attachmentDTO.setFileData(file.getBytes()); // Możliwe przekształcenie danych na byte[]
//                    attachmentDTO.setUpdateDate(LocalDateTime.now());
//                    attachmentDTOs.add(attachmentDTO);
//                }
//            }
//
//
//        }
        emailTemplateDTO.setAttachments(attachmentDTOs);
        emailService.updateEmailTemplate(emailTemplateDTO);

        redirectAttributes.addFlashAttribute("successMessage", "Szablon o id " + emailTemplateDTO.getEmailTemplateId() + " zaktualizowany");
        return "redirect:/email/home";
    }
}