package pl.bpwesley.TourOperator.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.bpwesley.TourOperator.email.entity.Attachment;
import pl.bpwesley.TourOperator.email.repository.AttachmentRepository;

import java.util.Optional;

@Controller
@RequestMapping("/email/attachments")
public class AttachmentController {
    private final AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentController(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable Long id) {
        // Po kliknieciu na zalacznik zostanie on pobrany z bazy i otworzony w nowej karcie
        Optional<Attachment> attachmentOpt = attachmentRepository.findById(id);
        if (attachmentOpt.isPresent()) {
            Attachment attachment = attachmentOpt.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + attachment.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(attachment.getFileData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
