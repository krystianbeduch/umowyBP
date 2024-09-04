package pl.bpwesley.TourOperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.bpwesley.TourOperator.model.Client;
import pl.bpwesley.TourOperator.repository.ClientRepository;

import java.util.List;

@Controller
public class HomeController {
//    static List<Client> clientList = new ArrayList<>();
//    static {
//        clientList.add(
//                new Client("Zakład Opieki Zdrowotnej Szpital im. Jana Pawła II we Włoszczowie", "ul. Żeromskiego", "28", "29-100", "Włoszczowa",
//                        new PickupLocation())
//        );
//        clientList.add(
//                new Client("Zespół Szkolno-Przedszkolny nr 5", "ul. Św.Barbary", "32", "42-200", "Częstochowa",
//                        new PickupLocation("Plac", "ul. Jakas tam", "30", "42-200", "Częstochowa"))
//        );
//        clientList.add(
//                new Client("SP2", "ul. Baczyńskiego", "2", "42-224", "Częstochowa",
//                        new PickupLocation())
//        );
//        clientList.add(
//                new Client("ZST", "al. Jana Pawła II", "126/130", "42-220", "Częstochowa",
//                        new PickupLocation())
//        );
//    }

    private final ClientRepository clientRepository;

    @Autowired
    public HomeController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Client> clientList = clientRepository.findAll();
        model.addAttribute("clients", clientList);
        return "home";
    }

    @GetMapping("/add-client")
    public String addClientForm(Model model) {
        List<Client> clientList = clientRepository.findAll();
        model.addAttribute("clients", clientList);
        return "add_client";
    }

    @GetMapping("/edit-client")
    public String editClientForm(Model model) {
        List<Client> clientList = clientRepository.findAll();
        model.addAttribute("clients", clientList);
        return "edit_client";
    }

    @GetMapping("/delete-client")
    public String deleteClientForm(Model model) {
        List<Client> clientList = clientRepository.findAll();
        model.addAttribute("clients", clientList);
        return "delete_client";
    }
}
