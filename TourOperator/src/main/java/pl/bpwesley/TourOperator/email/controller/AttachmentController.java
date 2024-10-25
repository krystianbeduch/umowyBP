package pl.bpwesley.TourOperator.email.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import pl.bpwesley.TourOperator.email.entity.Attachment;
import pl.bpwesley.TourOperator.email.repository.AttachmentRepository;
import pl.bpwesley.TourOperator.email.service.AttachmentService;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/email/attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentController(AttachmentService attachmentService, AttachmentRepository attachmentRepository) {
        this.attachmentService = attachmentService;
        this.attachmentRepository = attachmentRepository;
    }

    @GetMapping("/upload")
    public String showUploadFile() {
        return "email/upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            Attachment savedAttachment = attachmentService.saveAttachment(file);
            model.addAttribute("successMessage", "Plik " + savedAttachment.getFilename() + " został zapisany!");
        }
        catch (IOException e) {
            model.addAttribute("errorMessage", "Wystąpił błąd podczas zapisywania pliku.");
        }
        return "email/home";
    }

    @GetMapping("/{id}")
    public void downloadFile(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Attachment attachment = attachmentService.getAttachmentById(id);
        if (attachment != null) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getFilename() + "\"");
            response.getOutputStream().write(attachment.getFileData());
            response.getOutputStream().flush();
        }
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable Long id) {
        Optional<Attachment> attachmentOpt = attachmentRepository.findById(id);
        if (attachmentOpt.isPresent()) {
            Attachment attachment = attachmentOpt.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(attachment.getFileData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
