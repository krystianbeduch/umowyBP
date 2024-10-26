package pl.bpwesley.TourOperator.email.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.bpwesley.TourOperator.email.dto.AttachmentDto;
import pl.bpwesley.TourOperator.email.entity.Attachment;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;
import pl.bpwesley.TourOperator.email.repository.AttachmentRepository;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttachmentService {
    private final EmailTemplateRepository emailTemplateRepository;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentService(AttachmentRepository attachmentRepository, EmailTemplateRepository emailTemplateRepository) {
        this.attachmentRepository = attachmentRepository;
        this.emailTemplateRepository = emailTemplateRepository;
    }

    public Attachment saveAttachment(MultipartFile file) throws IOException {
        // używa metoda ktora bedzie usunieta
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
    public List<AttachmentDto> processAttachments(List<MultipartFile> newAttachments) throws IOException {
        List<AttachmentDto> attachmentDtos = new ArrayList<>();

        // Przetworzenie nowych zalacznikow
        if (newAttachments != null && !newAttachments.isEmpty()) {
            for (MultipartFile file : newAttachments) {
                if (!file.isEmpty()) {
                    AttachmentDto attachmentDto = new AttachmentDto();
                    attachmentDto.setFilename(file.getOriginalFilename());
                    attachmentDto.setFileData(file.getBytes());
                    attachmentDto.setUpdateDate(LocalDateTime.now());
                    attachmentDtos.add(attachmentDto);
                }
            }
        }
        return attachmentDtos;
    }
}
