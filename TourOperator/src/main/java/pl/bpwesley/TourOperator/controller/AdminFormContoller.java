package pl.bpwesley.TourOperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.bpwesley.TourOperator.dto.ClientDTO;
import pl.bpwesley.TourOperator.exception.ClientAlreadyExistsException;
import pl.bpwesley.TourOperator.exception.ClientNotFoundException;
import pl.bpwesley.TourOperator.service.ClientService;

@Controller
@RequestMapping("/admin/form") // Prefiks dla formularzy /admin/form
public class AdminFormContoller {

    private final ClientService clientService;

    @Autowired
    public AdminFormContoller(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/add")
    private String showAddClientForm(Model model) {
        // Zaladuj liste klientow
        model.addAttribute("clients", clientService.getClientList());

        model.addAttribute("client", new ClientDTO());

        // Zwroc admin/add_client.html
        return "admin/add_client";
    }

    @PostMapping("/add") // obsluga wyslania formularza add-client
    private String addClient(@ModelAttribute("client") ClientDTO clientDTO, Model model) {
        try {
            // Dodanie klienta do bazy
            clientService.addClient(clientDTO);
            return "redirect:/admin/"; // przekierowanie na strone glowna po dodaniu klienta
        }
        catch (ClientAlreadyExistsException e) {
            // Klient istnieje, wyswietl komunikat o bledzie
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("clients", clientService.getClientList());

            // Zaladuj wpisane dane do formularza
            model.addAttribute("client", clientDTO);
            return "admin/add_client"; // Zwroc admin/add_client.html
        }
    }

    @GetMapping("/delete")
    private String showDeleteClientForm(Model model) {
        // Zaladuj liste klientow
        model.addAttribute("clients", clientService.getClientList());
        model.addAttribute("selectedClientNumber", null);
        return "admin/delete_client"; // Zwroc admin/delete_client.html
    }

    @DeleteMapping("/delete/{clientNumber}") // obsluga wyslania formularza delete-client
    private String deleteClient(@PathVariable Long clientNumber, Model model) {
        try {
            clientService.deleteClient(clientNumber);
            // Przekierowanie na strone glowna po usunieciu klienta
            return "redirect:/admin/";
        }
        catch (ClientNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("clients", clientService.getClientList());
            return "admin/delete_client"; // Zwroc admin/delete_client.html
        }
    }

    @GetMapping("/edit")
    public String showEditClientForm(Model model) {
        // Zaladuj liste klientow
        model.addAttribute("clients", clientService.getClientList());

        // Zwroc admin/edit_client.html
        return "admin/edit_client";
    }

    @PutMapping("/edit")
    public String updateClient(@ModelAttribute("client") ClientDTO updatedClientDTO, Model model) {
        try {
            // Zaktualizuj dane klienta
            clientService.updateClientData(updatedClientDTO);
            return "redirect:/admin/";
        }
        catch (ClientNotFoundException | ClientAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("clients", clientService.getClientList());
            return "admin/edit_client"; // Zwroc admin/edit_client.html
        }
    }
}