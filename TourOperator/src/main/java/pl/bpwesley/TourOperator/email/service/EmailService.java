package pl.bpwesley.TourOperator.email.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDTO;
import pl.bpwesley.TourOperator.email.entity.Attachment;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;
import pl.bpwesley.TourOperator.email.entity.EmailTemplateVariable;
import pl.bpwesley.TourOperator.email.exception.EmailTemplateNotFoundException;
import pl.bpwesley.TourOperator.email.mapper.EmailTemplateMapper;
import pl.bpwesley.TourOperator.email.repository.AttachmentRepository;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateRepository;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateVariableRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailService {
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailTemplateMapper emailTemplateMapper = EmailTemplateMapper.INSTANCE;
    private final EmailTemplateVariableRepository emailTemplateVariableRepository;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    public EmailService(EmailTemplateRepository emailTemplateRepository, EmailTemplateVariableRepository emailTemplateVariableRepository, AttachmentRepository attachmentRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailTemplateVariableRepository = emailTemplateVariableRepository;
        this.attachmentRepository = attachmentRepository;
    }

//    @Transactional
    public List<EmailTemplateDTO> getEmailTemplateList() {
        return emailTemplateRepository.findAllByOrderByUpdateDateDesc().stream()
                .map(emailTemplateMapper::emailTemplateToEmailTemplateDTO)
                .collect(Collectors.toList());
    }

//    @Transactional
    public Optional<EmailTemplateDTO> getEmailTemplateById(Long emailTemplateId) {
        // Znajdz szablon, zmapuj na DTO i zwroc go
        return emailTemplateRepository.findById(emailTemplateId).
                map(emailTemplateMapper::emailTemplateToEmailTemplateDTO);
    }

//    @Transactional
    public void updateEmailTemplate(EmailTemplateDTO emailTemplateDTO) {
        if (emailTemplateRepository.existsById(emailTemplateDTO.getEmailTemplateId())) {
            EmailTemplate emailTemplate = emailTemplateMapper.emailTemplateDTOToEmailTemplate(emailTemplateDTO);

            // Przetwarzanie zalacznikow jesli sa obecne w DTO
            if (emailTemplateDTO.getAttachments() != null && !emailTemplateDTO.getAttachments().isEmpty()) {
                List<Attachment> attachments = emailTemplateDTO.getAttachments().stream()
                        .map(dto -> new Attachment(dto.getFilename(), dto.getFileData(), LocalDateTime.now(), emailTemplate))
                        .collect(Collectors.toList());
                emailTemplate.setAttachments(attachments);
                emailTemplateRepository.save(emailTemplate);
//                attachmentRepository.saveAll(attachments);
            }
//            if (emailTemplateDTO.getAttachments() != null && !emailTemplateDTO.getAttachments().isEmpty()) {
//                List<Attachment> newAttachments = new ArrayList<>();
//
//                for (Attachment attachment : emailTemplateDTO.getAttachments()) {
//                    Optional<Attachment> existingAttachment = attachmentRepository.findById(attachment.getAttachmentId());
//                    if (existingAttachment.isEmpty()) {
//                        newAttachments.add(attachment);
//                    }
//                }
//
//                // Ustaw nowe załączniki tylko, jeżeli są
//                if (!newAttachments.isEmpty()) {
//                    emailTemplate.setAttachments(newAttachments);
//                    //attachmentRepository.saveAll(newAttachments); // Zapisujemy nowe załączniki do bazy
//                }
//            }

//            emailTemplateRepository.save(emailTemplate);
        }
        else {
            throw new EmailTemplateNotFoundException("BŁĄD AKTUALIZACJI: szablon o id " + emailTemplateDTO.getEmailTemplateId() + " nie istnieje");
        }
    }

    // VARIABLES
    public List<EmailTemplateVariable> getEmailTemplateVariablesByTemplateId(Long emailTemplateId) {
        return emailTemplateVariableRepository.findByEmailTemplate_EmailTemplateId(emailTemplateId);
    }
}