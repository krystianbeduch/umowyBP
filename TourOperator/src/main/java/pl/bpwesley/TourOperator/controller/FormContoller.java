package pl.bpwesley.TourOperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.bpwesley.TourOperator.model.Client;
import pl.bpwesley.TourOperator.repository.ClientRepository;

@Controller
@RequestMapping("/form")
public class FormContoller {

    private final ClientRepository clientRepository;

    @Autowired
    public FormContoller(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    private String formPage() {
        return "add_client";
    }

    @PostMapping
    private String addClient(Client client) {
        clientRepository.save(client);
        return "redirect:/";
    }
}
