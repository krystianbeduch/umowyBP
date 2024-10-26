package pl.bpwesley.TourOperator.email;

import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.bpwesley.TourOperator.email.entity.Attachment;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;
import pl.bpwesley.TourOperator.email.entity.EmailTemplateVariable;
import pl.bpwesley.TourOperator.email.entity.Variable;
import pl.bpwesley.TourOperator.email.repository.AttachmentRepository;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateRepository;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateVariableRepository;
import pl.bpwesley.TourOperator.email.repository.VariableRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Profile("create-simple-email-template")
@Configuration
public class DbInit implements CommandLineRunner {
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailTemplateVariableRepository emailTemplateVariableRepository;
    private final VariableRepository variableRepository;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    public DbInit(EmailTemplateRepository emailTemplateRepository, EmailTemplateVariableRepository emailTemplateVariableRepository, VariableRepository variableRepository, AttachmentRepository attachmentRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailTemplateVariableRepository = emailTemplateVariableRepository;
        this.variableRepository = variableRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Variable clientNameVar = new Variable("client_name", "Imie klienta");
            Variable tourNameVar = new Variable("tour_name", "Nazwa_wycieczki");
            Variable tourIdVar = new Variable( "tour_id", "Id wycieczki");
            Variable tourLocationVar = new Variable("tour_location", "Tour location");
            Variable startDateVar = new Variable("start_date", "dataod: 44");
            Variable endDateVar = new Variable("end_date", "datado: 44");
            Variable advanceVar = new Variable("advance", "kwota zaliczki 200zl");
            Variable advanceDeadlineVar = new Variable("advance_deadline", "zaliczka termin");
            Variable remainingAmmountVar = new Variable("remaining_ammount", "Pozostala kwota do zaplaty 200zl");
            Variable remainingAmmountDeadlineVar = new Variable("remaining_amount_deadline", "pozostaala kwota deadline");
            Variable departureTimeVar = new Variable("departure_time", "godzina wyjazd: 06:00");
            Variable arrivalTimeVar = new Variable( "arrival_time", "godzina przyjazd: 21:00");

            variableRepository.saveAll(Arrays.asList(
                    clientNameVar, tourNameVar, tourIdVar, tourLocationVar, startDateVar, endDateVar,
                    advanceVar, advanceDeadlineVar, remainingAmmountVar, remainingAmmountDeadlineVar,
                    departureTimeVar, arrivalTimeVar
            ));


            Path filePath = Paths.get("src/main/resources/templates/email_templates/reservation_confirmation.html");
            String content = Files.readString(filePath);
            EmailTemplate reservationConfirmationTemplate = new EmailTemplate("Potwierdzenie rezerwacji", null, content ,LocalDateTime.now());
            reservationConfirmationTemplate.setTemplateSubject(reservationConfirmationTemplate.getTemplateName() + " ${tour_name} ${tour_id}");

            filePath = Paths.get("src/main/resources/templates/email_templates/advance_payment_confirmation.html");
            content = Files.readString(filePath);
            EmailTemplate advancePaymentConfirmationTemplate = new EmailTemplate("Potwierdzenie płatności zaliczki", null, content, LocalDateTime.now());
            advancePaymentConfirmationTemplate.setTemplateSubject(advancePaymentConfirmationTemplate.getTemplateName() + " za ${tour_name} ${tour_id}");

            filePath = Paths.get("src/main/resources/templates/email_templates/payment_of_total_confirmation.html");
            content = Files.readString(filePath);
            EmailTemplate paymentOfTotalConfirmationTemplate = new EmailTemplate("Potwierdzenie płatności całości", null, content, LocalDateTime.now());
            paymentOfTotalConfirmationTemplate.setTemplateSubject(paymentOfTotalConfirmationTemplate.getTemplateName() + " za ${tour_name} ${tour_id}");

            filePath = Paths.get("src/main/resources/templates/email_templates/meeting-point-reminder.html");
            content = Files.readString(filePath);
            EmailTemplate meetingPointReminderTemplate = new EmailTemplate("Przypomnienie o zbiórce na wycieczkę", null, content, LocalDateTime.now());
            meetingPointReminderTemplate.setTemplateSubject(meetingPointReminderTemplate.getTemplateName() + " za ${tour_name} ${tour_id}");

             // Zapisz szablony do bazy danych
            emailTemplateRepository.saveAll(Arrays.asList(
                    reservationConfirmationTemplate,
                    advancePaymentConfirmationTemplate,
                    paymentOfTotalConfirmationTemplate,
                    meetingPointReminderTemplate
            ));

            // Powiązania zmiennych z szablonami
            List<EmailTemplateVariable> emailTemplateVariables = new ArrayList<>();

            // Potwierdzenie rezerwacji
            emailTemplateVariables.add(new EmailTemplateVariable(reservationConfirmationTemplate, clientNameVar));
            emailTemplateVariables.add(new EmailTemplateVariable(reservationConfirmationTemplate, tourNameVar));
            emailTemplateVariables.add(new EmailTemplateVariable(reservationConfirmationTemplate, tourIdVar));
            emailTemplateVariables.add(new EmailTemplateVariable(reservationConfirmationTemplate, tourLocationVar));
            emailTemplateVariables.add(new EmailTemplateVariable(reservationConfirmationTemplate, startDateVar));
            emailTemplateVariables.add(new EmailTemplateVariable(reservationConfirmationTemplate, endDateVar));
            emailTemplateVariables.add(new EmailTemplateVariable(reservationConfirmationTemplate, advanceVar));
            emailTemplateVariables.add(new EmailTemplateVariable(reservationConfirmationTemplate, advanceDeadlineVar));
            emailTemplateVariables.add(new EmailTemplateVariable(reservationConfirmationTemplate, remainingAmmountVar));
            emailTemplateVariables.add(new EmailTemplateVariable(reservationConfirmationTemplate, remainingAmmountDeadlineVar));
            emailTemplateVariables.add(new EmailTemplateVariable(reservationConfirmationTemplate, departureTimeVar));
            emailTemplateVariables.add(new EmailTemplateVariable(reservationConfirmationTemplate, arrivalTimeVar));

            // Potwierdzenie płatności zaliczki
            emailTemplateVariables.add(new EmailTemplateVariable(advancePaymentConfirmationTemplate, clientNameVar));
            emailTemplateVariables.add(new EmailTemplateVariable(advancePaymentConfirmationTemplate, tourNameVar));
            emailTemplateVariables.add(new EmailTemplateVariable(advancePaymentConfirmationTemplate, tourIdVar));
            emailTemplateVariables.add(new EmailTemplateVariable(advancePaymentConfirmationTemplate, tourLocationVar));
            emailTemplateVariables.add(new EmailTemplateVariable(advancePaymentConfirmationTemplate, advanceVar));
            emailTemplateVariables.add(new EmailTemplateVariable(advancePaymentConfirmationTemplate, remainingAmmountVar));
            emailTemplateVariables.add(new EmailTemplateVariable(advancePaymentConfirmationTemplate, remainingAmmountDeadlineVar));

            // Potwierdzenie płatności całości
            emailTemplateVariables.add(new EmailTemplateVariable(paymentOfTotalConfirmationTemplate, clientNameVar));
            emailTemplateVariables.add(new EmailTemplateVariable(paymentOfTotalConfirmationTemplate, tourNameVar));
            emailTemplateVariables.add(new EmailTemplateVariable(paymentOfTotalConfirmationTemplate, tourIdVar));
            emailTemplateVariables.add(new EmailTemplateVariable(paymentOfTotalConfirmationTemplate, tourLocationVar));
            emailTemplateVariables.add(new EmailTemplateVariable(paymentOfTotalConfirmationTemplate, startDateVar));

            // Przypomnienie o zbiórce
            emailTemplateVariables.add(new EmailTemplateVariable(meetingPointReminderTemplate, clientNameVar));
            emailTemplateVariables.add(new EmailTemplateVariable(meetingPointReminderTemplate, tourNameVar));
            emailTemplateVariables.add(new EmailTemplateVariable(meetingPointReminderTemplate, tourIdVar));
            emailTemplateVariables.add(new EmailTemplateVariable(meetingPointReminderTemplate, tourLocationVar));
            emailTemplateVariables.add(new EmailTemplateVariable(meetingPointReminderTemplate, startDateVar));
            emailTemplateVariables.add(new EmailTemplateVariable(meetingPointReminderTemplate, departureTimeVar));


            // Zapisz powiązania do bazy danych
            emailTemplateVariableRepository.saveAll(emailTemplateVariables);

            // Dodaj zalaczniki
            addAttachmentsToTemplate(reservationConfirmationTemplate,
                    "Andrzejki-w-stylu-Country-3545i.pdf", "OWU_BP_Wesley.pdf",
                    "OWU_PZU_NNW.pdf", "Polityka_prywatnosci.pdf", "Regulamin_serwisu.pdf", "Umowa.pdf");
            addAttachmentsToTemplate(advancePaymentConfirmationTemplate, "Umowa.pdf");
            // addAttachmentsToTemplate(paymentOfTotalConfirmationTemplate, "Umowa.pdf");
            addAttachmentsToTemplate(meetingPointReminderTemplate, "Umowa.pdf");

            // Zapisanie załączników
            emailTemplateRepository.saveAll(Arrays.asList(
                    reservationConfirmationTemplate,
                    advancePaymentConfirmationTemplate,
                    paymentOfTotalConfirmationTemplate,
                    meetingPointReminderTemplate
            ));

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addAttachmentsToTemplate(EmailTemplate emailTemplate, String... fileNames) {
        for (String fileName : fileNames) {
            try {
                Path filePath = Paths.get("src/main/resources/templates/email_templates/attachments/" + fileName);
                byte[] data = Files.readAllBytes(filePath);
                Attachment attachment = new Attachment(fileName, data, LocalDateTime.now(), emailTemplate);
                attachmentRepository.save(attachment); // Zapisz załącznik w repozytorium
                if (emailTemplate.getAttachments() == null) {
                    emailTemplate.setAttachments(new ArrayList<>());
                }
                emailTemplate.getAttachments().add(attachment); // Dodaj załącznik do szablonu
            } catch (IOException e) {
                System.err.println("Błąd przy wczytywaniu załącznika: " + fileName);
                e.printStackTrace();
            }
        }
    }
}
