package pl.bpwesley.TourOperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bpwesley.TourOperator.model.Client;
import pl.bpwesley.TourOperator.repository.ClientRepository;

@RestController // klasa jest kontrolerem REST, ktory obsluguje zapytania HTTP i zwraca dane
@RequestMapping("/api/client") // bazowy URL dla wszystkich metod w tym kontrolerze
public class ClientApiController {
    private final ClientRepository clientRepository;

    @Autowired // automatyczne wstrzykniecie instancji klasy do kontrolera
    public ClientApiController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/{clientNumber}") // metoda odpowiada na zapytania GET na podany mapping
    public Client getClientByNumber(@PathVariable("clientNumber") Long clientNumber) {
    //@PathVariable - {clientNumber} z URL jest przekazywana do metody jako argument
        return clientRepository.findByClientNumber(clientNumber)
                .orElse(null); // jesli nie znajdziesz klienta - 404
    }

    @GetMapping("/name/{name}")
    public Client getClientByName(@PathVariable("name") String name) {
        return clientRepository.findByNameIgnoreCaseAndAccent(name)
                .orElse(null); // jesli nie znajdziesz klienta - 404
    }
}
