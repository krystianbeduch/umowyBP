package pl.bpwesley.TourOperator.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateRepository;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmailService {
    private final EmailTemplateRepository emailTemplateRepository;
    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(EmailTemplateRepository emailTemplateRepository, JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
//        this.htmlFileReader = htmlFileReader;
    }

    public List<EmailTemplate> getEmailTemplateList() {
        return emailTemplateRepository.findAll();
    }

    public String getEmailTemplateName(Long templateId) {
        Optional<EmailTemplate> emailTemplate = emailTemplateRepository.findById(templateId);

        // Zwroc nazwe szablonu jesli istnieje
        return emailTemplate.map(EmailTemplate::getName).orElse("");
    }

    public String getEmailTemplateContent(Long templateId) {
        Optional<EmailTemplate> emailTemplate = emailTemplateRepository.findById(templateId);

        // Zwroc nazwe szablonu jesli istnieje
        return emailTemplate.map(EmailTemplate::getContent).orElse("");
    }
    public String getEmailTemplateContentBody(Long templateId, Map<String, Object> variables) {
        Optional<EmailTemplate> emailTemplate = emailTemplateRepository.findById(templateId);


        return null;

        // Zwroc zawartosc szablonu jesli istnieje
//        return templateEngine.process(getEmailTemplateContent(templateId), context);
    }

    public void updateEmailTemplateContent(EmailTemplate emailTemplate) {
        if (emailTemplateRepository.existsById(emailTemplate.getEmailTemplateId())) {
            emailTemplateRepository.save(emailTemplate);
        }
        else {
            throw new EntityNotFoundException("Szablon od id " + emailTemplate.getEmailTemplateId() + "nie istnieje");
        }
    }
}