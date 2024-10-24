package pl.bpwesley.TourOperator.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.bpwesley.TourOperator.email.entity.Attachment;
import pl.bpwesley.TourOperator.email.repository.AttachmentRepository;

import java.io.IOException;

@Service
public class AttachmentService {
    private AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
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
}
