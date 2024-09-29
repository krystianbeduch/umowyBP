package pl.bpwesley.TourOperator.groupsAdminPanel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.bpwesley.TourOperator.groupsAdminPanel.entity.Client;
import pl.bpwesley.TourOperator.groupsAdminPanel.repository.ClientRepository;

import java.util.List;

@Controller
@RequestMapping("/admin") // Wszystkie sciezki w tym kontrolerze beda prefiksowane /admin
public class AdminController {
    private final ClientRepository clientRepository;

    @Autowired
    public AdminController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/") // strona dostepna pod URL /admin/
    public String home(Model model) {
        // Zaladuj liste klientow
        List<Client> clientList = clientRepository.findAllByOrderByClientNumberAsc();
        model.addAttribute("clients", clientList);

        // Zwroc admin/home.html
        return "admin/home";
    }
}
