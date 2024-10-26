package pl.bpwesley.TourOperator.email.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDto;
import pl.bpwesley.TourOperator.email.entity.Attachment;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;
import pl.bpwesley.TourOperator.email.entity.EmailTemplateVariable;
import pl.bpwesley.TourOperator.email.exception.EmailTemplateNotFoundException;
import pl.bpwesley.TourOperator.email.mapper.AttachmentMapper;
import pl.bpwesley.TourOperator.email.mapper.EmailTemplateMapper;
import pl.bpwesley.TourOperator.email.repository.AttachmentRepository;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateRepository;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateVariableRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailService {
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailTemplateMapper emailTemplateMapper = EmailTemplateMapper.INSTANCE;
    private final EmailTemplateVariableRepository emailTemplateVariableRepository;
    private final AttachmentService attachmentService;
    private final AttachmentMapper attachmentMapper = AttachmentMapper.INSTANCE;

    @Autowired
    public EmailService(EmailTemplateRepository emailTemplateRepository, EmailTemplateVariableRepository emailTemplateVariableRepository, AttachmentService attachmentService) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailTemplateVariableRepository = emailTemplateVariableRepository;
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

            // Ustawienie daty aktualizacji
//            emailTemplate.setUpdateDate(LocalDateTime.now());
//            List<Attachment> attachments = attachmentMapper.attachmentToAttachmentDto(emailTemplate);

            // Powiazanie zalacznikow z szablonem
//            if (emailTemplateDTO.getAttachmentDtos() != null && !emailTemplateDTO.getAttachmentDtos().isEmpty()) {
//                List<Attachment> attachments = emailTemplateDTO.getAttachmentDtos().stream()
//                        .map(dto -> new Attachment(dto.getFilename(), dto.getFileData(), LocalDateTime.now(), emailTemplate))
//                        .collect(Collectors.toList());
            List<Attachment> attachments = attachmentService.mapAndAssignEmailTemplate(emailTemplateDTO.getAttachmentDtos(), emailTemplate);
//            if (emailTemplateDTO.getAttachmentDtos() != null && !emailTemplateDTO.getAttachmentDtos().isEmpty()) {
//                List<Attachment> attachments = emailTemplateDTO.getAttachmentDtos().stream()
//                        .map(dto -> {
//                            Attachment attachment = attachmentMapper.attachmentDtoToAttachment(dto);
//                            attachment.setEmailTemplate(emailTemplate); // Przypisanie szablonu do zalacznika
//                            return attachment;
//                        })
//                        .collect(Collectors.toList());
//

//            }
            emailTemplate.setAttachments(attachments);
            emailTemplateRepository.save(emailTemplate);
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