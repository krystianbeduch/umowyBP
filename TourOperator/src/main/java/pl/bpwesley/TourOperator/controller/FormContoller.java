package pl.bpwesley.TourOperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.bpwesley.TourOperator.dto.ClientDTO;
import pl.bpwesley.TourOperator.entity.Client;
import pl.bpwesley.TourOperator.service.ClientService;

@Controller
@RequestMapping("/form")
public class FormContoller {

    private final ClientService clientService;

    @Autowired
    public FormContoller(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/add")
    private String showAddClientForm(Model model) {
        // Zaladuj liste klientow
        model.addAttribute("clients", clientService.getClientList());

        // Zwroc add_client.html
        return "add_client";
    }

    @PostMapping("/add") // obsluga wyslania formularza add-client
    private String addClient(@ModelAttribute("client") ClientDTO clientDTO, Model model) {
        // Sprwadzenie czy klient o takiej nazwie juz istnieje
        if (clientService.clientExistsByName(clientDTO.getName())) {
            // Klient istnieje, wyswietl komunikat o bledzie
            model.addAttribute("errorMessage", "Klient o podanej nazwie już istnieje");
            model.addAttribute("clients", clientService.getClientList());
            return "add_client";
        }
        // Dodanie klienta do bazy
        clientService.addClient(clientDTO);
        return "redirect:/"; // przekierowanie na strone glowna po dodaniu klienta
    }

    @GetMapping("/delete")
    private String showDeleteClientForm(Model model) {
        // Zaladuj liste klientow
        model.addAttribute("clients", clientService.getClientList());
        model.addAttribute("selectedClientNumber", null);

        // Zwroc delete_client.html
        return "delete_client";
    }

    @DeleteMapping("/delete/{clientNumber}") // obsluga wyslania formularza delete-client
    private String deleteClient(@PathVariable Long clientNumber, Model model) {
        if (clientService.clientExistsByClientNumber(clientNumber)) {
            clientService.deleteClient(clientNumber);
            // Przekierowanie na strone glowna po usunieciu klienta
            return "redirect:/";
        }
        else {
            model.addAttribute("errorMessage", "Klient o numerze " + clientNumber + " nie istnieje");
            model.addAttribute("clients", clientService.getClientList());

            // Zwroc delete_client.html
            return "delete_client";
        }
    }

    @GetMapping("/edit")
    public String showEditClientForm(Model model) {
        // Zaladuj liste klientow
        model.addAttribute("clients", clientService.getClientList());

        // Zwroc edit_client.html
        return "edit_client";
    }

    @PutMapping("/edit")
    public String updateClient(@ModelAttribute("client") ClientDTO updatedClientDTO, Model model) {
        // Sprawdz czy klient o podanym numerze istnieje
        if (!clientService.clientExistsByClientNumber(updatedClientDTO.getClientNumber())) {
            model.addAttribute("errorMessage", "Klient o numerze " + updatedClientDTO.getClientNumber() + " nie istnieje");
            model.addAttribute("clients", clientService.getClientList());
            return "edit_client";
        }

        // Sprawdz czy nazwa jest unikalna
        if (clientService.getClientByName(updatedClientDTO.getName())
                .filter(client -> !client.getClientNumber().equals(updatedClientDTO.getClientNumber()))
                .isPresent()) {
            model.addAttribute("errorMessage", "Klient o nazwie - " + updatedClientDTO.getName() + " - już istnieje");
            model.addAttribute("clients", clientService.getClientList());
            return "edit_client";
        }

        // Zaktualizuj dane klienta
        clientService.updateClientData(updatedClientDTO);
        return "redirect:/";
    }
}