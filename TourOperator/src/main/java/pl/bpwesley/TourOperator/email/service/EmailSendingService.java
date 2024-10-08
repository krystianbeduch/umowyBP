package pl.bpwesley.TourOperator.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public class EmailSendingService {
    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Autowired
    public EmailSendingService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }



    public void sendEmailWithReservationConfirmation(String to, String subject, Map<String, Object> variables) throws MessagingException, UnsupportedEncodingException {
        // Utworz i przetworz szablon emaila
        Context context = new Context();
        context.setVariables(variables);
        String body = templateEngine.process("email_templates/reservation_confirmation.html", context);

        // Utworz wiadomosc email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(new InternetAddress("beduch.krystian@gmail.com", "Krystian"));
        helper.setTo(to);
        helper.setSubject(subject + variables.get("tour_name"));
        helper.setText(body, true);

        // Dodaj zalaczniki pdf
        String[] attachments = {
                "Andrzejki-w-stylu-Country-3545i.pdf",
                "OWU_BP_Wesley.pdf",
                "OWU_PZU_NNW.pdf",
                "Polityka_prywatnosci.pdf",
                "Regulamin_serwisu.pdf",
                "Umowa.pdf"
        };

        for (String fileName : attachments) {
            ClassPathResource attachment = new ClassPathResource("templates/email_templates/attachments/" + fileName);
            helper.addAttachment(attachment.getFilename(), attachment);
        }

        // Zaladuj logo do maila
        ClassPathResource image = new ClassPathResource("static/images/Logo_Wesley_mini.png");
        helper.addInline("logoImage", image);

        // Wyslij emaila
        mailSender.send(message);
    }

    public void sendEmailWithAdvancePaymentConfirmation(String to, String subject, Map<String, Object> variables) throws MessagingException, UnsupportedEncodingException {
        // Utworz i przetworz szablon emaila
        Context context = new Context();
        context.setVariables(variables);
        String body = templateEngine.process("email_templates/advance_payment_confirmation.html", context);

        // Utworz wiadomosc email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(new InternetAddress("beduch.krystian@gmail.com", "Krystian"));
        helper.setTo(to);
        helper.setSubject(subject + variables.get("tour_name"));
        helper.setText(body, true);

        // Dodaj zalaczniki pdf
        String[] attachments = {
                "Umowa.pdf"
        };

        for (String fileName : attachments) {
            ClassPathResource attachment = new ClassPathResource("templates/email_templates/attachments/" + fileName);
            helper.addAttachment(attachment.getFilename(), attachment);
        }

        // Zaladuj logo do maila
        ClassPathResource image = new ClassPathResource("static/images/Logo_Wesley_mini.png");
        helper.addInline("logoImage", image);

        // Wyslij emaila
        mailSender.send(message);
    }
}
