package pl.bpwesley.TourOperator.email.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.email.dto.AttachmentDto;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDto;
import pl.bpwesley.TourOperator.email.entity.EmailTemplateVariable;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class EmailSendingService {
    private final JavaMailSender mailSender;
    private final EmailService emailService;
    private final Configuration freemarkerConfig;

    @Autowired
    public EmailSendingService(JavaMailSender mailSender, EmailService emailService, Configuration freemarkerConfig) {
        this.mailSender = mailSender;
        this.emailService = emailService;
        this.freemarkerConfig = freemarkerConfig;
    }


    public void sendEmail(EmailTemplateDto emailTemplateDto, String to, String subject, List<EmailTemplateVariable> emailTemplateVariables, List<AttachmentDto> attachments) throws MessagingException, IOException {
        // Pobierz nazwe i html szablonu
        String templateContent = emailTemplateDto.getContent();
        String templateName = emailTemplateDto.getTemplateName();

        // Inicjalizuj szablon FreeMarker
        Template template = new Template(templateName, new StringReader(templateContent), freemarkerConfig);
        StringWriter writer = new StringWriter();
        Map<String, Object> variables = new HashMap<>(); // mapa wymagana przez FreeMarker
        String tourName = "";
        String tourId = "";

        // Przypisz wartości zmiennych do mapy
        for (EmailTemplateVariable emailTemplateVariable : emailTemplateVariables) {
            String variableName = emailTemplateVariable.getVariable().getName();
            String variableValue = emailTemplateVariable.getVariable().getValue();
            variables.put(variableName, variableValue);

            if (variableName.equals("tour_name")) {
                tourName = variableValue;
            }
            else if(variableName.equals("tour_id")) {
                tourId = variableValue;
            }
        }

        try {
            // Wypełnij szablon danymi
            template.process(variables, writer);
        }
        catch (TemplateException | IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania szablonu", e);
        }

        String body = writer.toString();

        // Utworz wiadomosc email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(new InternetAddress("beduch.krystian@gmail.com", "Krystian"));
        helper.setTo(to);
        helper.setSubject(subject + tourName + " " + tourId);
        helper.setText(body, true);

        // Dodaj zalaczniki pdf
        if (attachments.size() > 0) {
            for (AttachmentDto attachment : emailTemplateDto.getAttachmentDtos()) {
                byte[] fileData = attachment.getFileData();
                String filename = attachment.getFilename();
                if (fileData != null && filename != null) {
                    ByteArrayDataSource dataSource = new ByteArrayDataSource(fileData, "application/octet-stream");
                    helper.addAttachment(filename, dataSource);
                }

            }
//            helper.addAttachment();
//            emailTemplateDto.getAttachmentDtos()
        }

//        if (attachments != null && !attachments.isEmpty()) {
//            for (String fileName : attachments) {
//                ClassPathResource attachment = new ClassPathResource("templates/email_templates/attachments/" + fileName);
//                File file = Objects.requireNonNull(attachment.getFile(), "Załącznik nie znaleziony");
//                helper.addAttachment(file.getName(), attachment);
//            }
//        }

        // Zaladuj logo do maila
        ClassPathResource image = new ClassPathResource("static/images/Logo_Wesley_mini.png");
        helper.addInline("logoImage", image);

        // Wyslij emaila
        mailSender.send(message);
    }
}
