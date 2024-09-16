package pl.bpwesley.TourOperator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/mail")
    @ResponseBody
    public String mail() {
        return "mail sent";
    }
    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin/home";
    }

}
