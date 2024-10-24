package pl.bpwesley.TourOperator.email.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDTO;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;
import pl.bpwesley.TourOperator.email.entity.EmailTemplateVariable;
import pl.bpwesley.TourOperator.email.exception.EmailTemplateNotFoundException;
import pl.bpwesley.TourOperator.email.mapper.EmailTemplateMapper;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateRepository;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateVariableRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmailService {
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailTemplateMapper emailTemplateMapper = EmailTemplateMapper.INSTANCE;
    private final EmailTemplateVariableRepository emailTemplateVariableRepository;

    @Autowired
    public EmailService(EmailTemplateRepository emailTemplateRepository, EmailTemplateVariableRepository emailTemplateVariableRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailTemplateVariableRepository = emailTemplateVariableRepository;
    }

    @Transactional
    public List<EmailTemplateDTO> getEmailTemplateList() {
        return emailTemplateRepository.findAllByOrderByUpdateDateDesc().stream()
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
            throw new EmailTemplateNotFoundException("BŁĄD AKTUALIZACJI: szablon o id " + emailTemplateDTO.getEmailTemplateId() + " nie istnieje");
        }
    }

    // VARIABLES
    public List<EmailTemplateVariable> getEmailTemplateVariablesByTemplateId(Long emailTemplateId) {
        return emailTemplateVariableRepository.findByEmailTemplate_Id(emailTemplateId);
    }
}