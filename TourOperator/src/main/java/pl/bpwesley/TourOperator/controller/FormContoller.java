package pl.bpwesley.TourOperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.bpwesley.TourOperator.entity.Client;
import pl.bpwesley.TourOperator.repository.ClientRepository;
import pl.bpwesley.TourOperator.service.ClientService;

import java.util.List;

@Controller
@RequestMapping("/form")
public class FormContoller {

    private final ClientRepository clientRepository;
    private final ClientService clientService;

    @Autowired
    public FormContoller(ClientRepository clientRepository, ClientService clientService) {
        this.clientRepository = clientRepository;
        this.clientService = clientService;
    }

    @GetMapping("/add")
    private String showAddClientForm(Model model) {
        // Zaladuj liste klientow
        model.addAttribute("clients", getClientList());

        // Zwroc add_client.html
        return "add_client";
    }

    @PostMapping("/add") // obsluga wyslania formularza add-client
    private String addClient(@ModelAttribute("client") Client client, Model model) {
        // Sprwadzenie czy klient o takiej nazwie juz istnieje
        Client existingClient = clientRepository.findByNameIgnoreCaseAndAccent(client.getName()).orElse(null);

        if (existingClient != null) {
            // Klient istnieje, wyswietl komunikat o bledzie
            model.addAttribute("errorMessage", "Klient o podanej nazwie już istnieje");
            model.addAttribute("clients", getClientList());
            return "add_client";
        }

        // INSERT INTO
        clientRepository.save(client);
        return "redirect:/"; // przekierowanie na strone glowna po dodaniu klienta
    }

    @GetMapping("/delete")
    private String showDeleteClientForm(Model model) {
        // Zaladuj liste klientow
        model.addAttribute("clients", getClientList());

        Long selectedClientNumber = null;
        model.addAttribute("selectedClientNumber", selectedClientNumber);

        // Zwroc delete_client.html
        return "delete_client";
    }

    @DeleteMapping("/delete/{clientNumber}") // obsluga wyslania formularza delete-client
//    @PostMapping("/delete/{clientNumber}") // obsluga wyslania formularza delete-client
    private String deleteClient(@PathVariable Long clientNumber, Model model) {
        if (clientRepository.existsById(clientNumber)) {
            // DELETE
            clientRepository.deleteById(clientNumber);
            return "redirect:/"; // przekierowanie na strone glowna po usunieciu klienta
        }
        else {
            model.addAttribute("errorMessage", "Klient o numerze " + clientNumber + " nie istnieje");
            model.addAttribute("clients", getClientList());
            return "delete_client";
        }

    }

    @GetMapping("/edit")
    public String showEditClientForm(Model model) {
        // Zaladuj liste klientow
        model.addAttribute("clients", getClientList());

        // Zwroc edit_client.html
        return "edit_client";
    }

    @PutMapping("/edit")
    public String updateClient(@ModelAttribute("client") Client updatedClient, Model model) {
        // Sprawdz czy klient istnieje
        Client existingClient = clientRepository.findByClientNumber(updatedClient.getClientNumber()).orElse(null);

        if (existingClient != null) {
            // Zaktualizuj dane i zapisz klienta w bazie
            clientService.updateClientData(existingClient, updatedClient);
            return "redirect:/";
        }
        else {
            model.addAttribute("errorMessage", "Klient o numerze " + updatedClient.getClientNumber() + " nie istnieje");
            model.addAttribute("clients", getClientList());
            return "edit_client";
        }
    }

    private List<Client> getClientList() {
        return clientRepository.findAllByOrderByClientNumberAsc();
    }
}
