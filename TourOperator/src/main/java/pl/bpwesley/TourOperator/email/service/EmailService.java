package pl.bpwesley.TourOperator.email.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDTO;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;
import pl.bpwesley.TourOperator.email.exception.EmailTemplateNotFoundException;
import pl.bpwesley.TourOperator.email.mapper.EmailTemplateMapper;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmailService {
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailTemplateMapper emailTemplateMapper = EmailTemplateMapper.INSTANCE;

    @Autowired
    public EmailService(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    public List<EmailTemplateDTO> getEmailTemplateList() {
        return emailTemplateRepository.findAll().stream()
                .map(emailTemplateMapper::emailTemplateToEmailTemplateDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmailTemplateDTO> getEmailTemplateById(Long emailTemplateId) {
        // Znajdz szablon, zmapuj na DTO i zwroc go
        return emailTemplateRepository.findById(emailTemplateId).
                map(emailTemplateMapper::emailTemplateToEmailTemplateDTO);
    }

    public void updateEmailTemplate(EmailTemplateDTO emailTemplateDTO) {
        if (emailTemplateRepository.existsById(emailTemplateDTO.getEmailTemplateId())) {
            EmailTemplate emailTemplate = emailTemplateMapper.emailTemplateDTOToEmailTemplate(emailTemplateDTO);
            emailTemplateRepository.save(emailTemplate);
        }
        else {
            throw new EmailTemplateNotFoundException("Szablon o id " + emailTemplateDTO.getEmailTemplateId() + " nie istnieje");
        }
    }
}