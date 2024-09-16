package pl.bpwesley.TourOperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.bpwesley.TourOperator.service.EmailService;

@Controller
public class MailController {

    private final EmailService emailService;

    @Autowired
    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @ResponseBody
    @GetMapping("/send-mail")
    public String sendMail() {
        emailService.sendSimpleMessage(
                "beduch_krystian@o2.pl",
                "Test",
                "cos tam"
        );
        return "mail_sent";
    }
}