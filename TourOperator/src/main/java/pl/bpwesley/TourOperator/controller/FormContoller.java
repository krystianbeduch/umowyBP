package pl.bpwesley.TourOperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.bpwesley.TourOperator.model.Client;
import pl.bpwesley.TourOperator.repository.ClientRepository;

import java.util.List;

@Controller
@RequestMapping("/form")
public class FormContoller {

    private final ClientRepository clientRepository;

    @Autowired
    public FormContoller(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/add")
    private String showAddClientForm(Model model) {
        // Zaladuj liste klientow
        model.addAttribute("clients", getClientList());

        // Zwroc add_client.html
        return "add_client";
    }

    @PostMapping("/add") // obsluga wyslania formularza add-client
    private String addClient(@ModelAttribute("client") Client client ) {
        // INSERT INTO
        clientRepository.save(client);
        return "redirect:/"; // przekierowanie na strone glowna po dodaniu klienta
    }

    @GetMapping("/delete")
    private String showDeleteClientForm(Model model) {
        // Zaladuj liste klientow
        model.addAttribute("clients", getClientList());

        // Zwroc delete_client.html
        return "delete_client";
    }

    @DeleteMapping("/delete/{clientNumber}") // obsluga wyslania formularza delete-client
    private String deleteClient(@PathVariable Long clientNumber) {
        if (clientRepository.existsById(clientNumber)) {
            // DELETE
            clientRepository.deleteById(clientNumber);
        }
        return "redirect:/"; // przekierowanie na strone glowna po usunieciu klienta
    }

    private List<Client> getClientList() {
        return clientRepository.findAll();
    }
}
