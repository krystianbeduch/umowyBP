package pl.bpwesley.TourOperator.email.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDto;
import pl.bpwesley.TourOperator.email.entity.Attachment;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;
import pl.bpwesley.TourOperator.email.exception.EmailTemplateNotFoundException;
import pl.bpwesley.TourOperator.email.mapper.EmailTemplateMapper;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailService {
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailTemplateMapper emailTemplateMapper = EmailTemplateMapper.INSTANCE;
    private final AttachmentService attachmentService;

    @Autowired
    public EmailService(EmailTemplateRepository emailTemplateRepository, AttachmentService attachmentService) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.attachmentService = attachmentService;
    }

    public List<EmailTemplateDto> getEmailTemplateList() {
        return emailTemplateRepository.findAllByOrderByUpdateDateDesc().stream()
                .map(emailTemplateMapper::emailTemplateToEmailTemplateDto)
                .collect(Collectors.toList());
    }

    public Optional<EmailTemplateDto> getEmailTemplateById(Long emailTemplateId) {
        // Znajdz szablon, zmapuj na DTO i zwroc go
        return emailTemplateRepository.findById(emailTemplateId).
                map(emailTemplateMapper::emailTemplateToEmailTemplateDto);
    }

    public void updateEmailTemplate(EmailTemplateDto emailTemplateDTO) {
        if (emailTemplateRepository.existsById(emailTemplateDTO.getEmailTemplateId())) {
            EmailTemplate emailTemplate = emailTemplateMapper.emailTemplateDtoToEmailTemplate(emailTemplateDTO);

            // Powiazanie zalacznikow z szablonem
            List<Attachment> attachments = attachmentService.mapAndAssignEmailTemplate(emailTemplateDTO.getAttachmentDtos(), emailTemplate);
            emailTemplate.setAttachments(attachments);

            emailTemplateRepository.save(emailTemplate);
        }
        else {
            throw new EmailTemplateNotFoundException("BŁĄD AKTUALIZACJI: szablon o id " + emailTemplateDTO.getEmailTemplateId() + " nie istnieje");
        }
    }
}