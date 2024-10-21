package pl.bpwesley.TourOperator.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Profile("create-simple-email-template")
@Configuration
public class DbInit implements CommandLineRunner {
    private final EmailTemplateRepository emailTemplateRepository;

    @Autowired
    public DbInit(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Path filePath = Paths.get("src/main/resources/templates/email_templates/reservation_confirmation.html");
            String content = Files.readString(filePath);
            emailTemplateRepository.save(
                    new EmailTemplate(
                            null,
                            "Potwierdzenie rezerwacji",
                            content,
                            LocalDateTime.now()
                    )
            );

            filePath = Paths.get("src/main/resources/templates/email_templates/advance_payment_confirmation.html");
            content = Files.readString(filePath);
            emailTemplateRepository.save(
                    new EmailTemplate(
                            null,
                            "Potwierdzenie płatności zaliczki",
                            content,
                            LocalDateTime.now()
                    )
            );

            filePath = Paths.get("src/main/resources/templates/email_templates/payment_of_total_confirmation.html");
            content = Files.readString(filePath);
            emailTemplateRepository.save(
                    new EmailTemplate(
                            null,
                            "Potwierdzenie płatności całości",
                            content,
                            LocalDateTime.now()
                    )
            );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
