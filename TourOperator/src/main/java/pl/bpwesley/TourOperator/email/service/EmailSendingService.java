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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Service
public class EmailSendingService {
    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;
    private final VariableService variableService;

    @Autowired
    public EmailSendingService(JavaMailSender mailSender, Configuration freemarkerConfig, VariableService variableService) {
        this.mailSender = mailSender;
        this.freemarkerConfig = freemarkerConfig;
        this.variableService = variableService;
    }

    public void sendEmail(EmailTemplateDto emailTemplateDto, String to) throws MessagingException, IOException {
        // Pobierz nazwe i html szablonu
        String templateContent = emailTemplateDto.getContent();
        String templateName = emailTemplateDto.getTemplateName();

        // Inicjalizuj szablon FreeMarker
        Template template = new Template(templateName, new StringReader(templateContent), freemarkerConfig);
        StringWriter writer = new StringWriter();

        // Przypisz wartosci zmiennych do mapy, mapa wymagana przez FreeMarker
        Map<String, String> variables = emailTemplateDto.getEmailTemplateVariablesAsMap();

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

        // Zastap zmienne w temacie
        emailTemplateDto.setTemplateSubject(variableService.replaceVariables(emailTemplateDto.getTemplateSubject(),  variableService.getVariableList()));

        helper.setSubject(emailTemplateDto.getTemplateSubject());
        helper.setText(body, true);

        // Dodaj zalaczniki pdf
        if (!emailTemplateDto.getAttachmentDtos().isEmpty()) {
            for (AttachmentDto attachment : emailTemplateDto.getAttachmentDtos()) {
                byte[] fileData = attachment.getFileData();
                String filename = attachment.getFilename();
                if (fileData != null && filename != null) {
                    ByteArrayDataSource dataSource = new ByteArrayDataSource(fileData, "application/octet-stream");
                    helper.addAttachment(filename, dataSource);
                }
            }
        }

        // Dodanie zalacznikow jako plikow loklanych
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
