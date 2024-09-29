package pl.bpwesley.TourOperator.email.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/email/home")
    public String emailHome() {
        return "email/home";
    }
    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin/home";
    }

}
