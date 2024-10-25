package pl.bpwesley.TourOperator.email.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.bpwesley.TourOperator.email.entity.Attachment;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;
import pl.bpwesley.TourOperator.email.repository.AttachmentRepository;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateRepository;

import java.io.IOException;

@Service
public class AttachmentService {
    private final EmailTemplateRepository emailTemplateRepository;
    private AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentService(AttachmentRepository attachmentRepository, EmailTemplateRepository emailTemplateRepository) {
        this.attachmentRepository = attachmentRepository;
        this.emailTemplateRepository = emailTemplateRepository;
    }

    public Attachment saveAttachment(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        byte[] fileData = file.getBytes();
        Attachment attachment = new Attachment(fileName, fileData);
        return attachmentRepository.save(attachment);
    }

    public Attachment getAttachmentById(Long attachmentId) {
        return attachmentRepository.findById(attachmentId).orElse(null);
    }

    @Transactional
    public void addAttachmentToTemplate(Long emailTemplateId, String filename, byte[] fileData) {
        EmailTemplate emailTemplate = emailTemplateRepository.findById(emailTemplateId).orElse(null);
        if (emailTemplate != null) {
            Attachment attachment = new Attachment(filename, fileData);
            emailTemplate.getAttachments().add(attachment);
            emailTemplateRepository.save(emailTemplate);
        }

    }
}
