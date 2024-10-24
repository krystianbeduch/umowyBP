package pl.bpwesley.TourOperator.email.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import pl.bpwesley.TourOperator.email.entity.Attachment;
import pl.bpwesley.TourOperator.email.service.AttachmentService;

import java.io.IOException;

@Controller
@RequestMapping("/email/attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @GetMapping("/upload")
    public String showUploadFile() {
        return "email/upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            Attachment savedAttachment = attachmentService.saveAttachment(file);
            model.addAttribute("successMessage", "Plik " + savedAttachment.getFileName() + " został zapisany!");
        }
        catch (IOException e) {
            model.addAttribute("errorMessage", "Wystąpił błąd podczas zapisywania pliku.");
        }
        return "email/upload";
    }

    @GetMapping("/{id}")
    public void downloadFile(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Attachment attachment = attachmentService.getAttachmentById(id);
        if (attachment != null) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"");
            response.getOutputStream().write(attachment.getFileData());
            response.getOutputStream().flush();
        }
    }
}
