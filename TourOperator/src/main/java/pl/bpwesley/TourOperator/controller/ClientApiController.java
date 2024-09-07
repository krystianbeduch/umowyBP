package pl.bpwesley.TourOperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bpwesley.TourOperator.entity.Client;
import pl.bpwesley.TourOperator.repository.ClientRepository;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController // klasa jest kontrolerem REST, ktory obsluguje zapytania HTTP i zwraca dane
@RequestMapping("/api/client") // bazowy URL dla wszystkich metod w tym kontrolerze
public class ClientApiController {
    private final ClientRepository clientRepository;

    @Autowired // automatyczne wstrzykniecie instancji klasy do kontrolera
    public ClientApiController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Metoda sprawdzajaca czy request pochodzi z przegladarki
    private boolean isBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null && userAgent.contains("Mozilla");
    }

    @GetMapping("/")
    public ResponseEntity<List<Client>> getAllClients(HttpServletRequest request) {
        if (isBrowser(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(clientRepository.findAll());
    }
    @GetMapping("/{clientNumber}") // metoda odpowiada na zapytania GET na podany mapping
    public ResponseEntity<Client> getClientByNumber(@PathVariable("clientNumber") Long clientNumber,
                                                    HttpServletRequest request) {
//    @PathVariable - {clientNumber} z URL jest przekazywana do metody jako argument
// Blokada spowodowala problem z auto uzupelnianiem formularza do usuwania
//        if (isBrowser(request)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
        return clientRepository.findByClientNumber(clientNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Client> getClientByName(@PathVariable("name") String name,
                                                  HttpServletRequest request) {
//        if (isBrowser(request)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
        return clientRepository.findByNameIgnoreCaseAndAccent(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }
    @DeleteMapping("/{clientNumber}")
    public ResponseEntity<Void> deleteClient(@PathVariable("clientNumber") Long clientNumber,
                                             HttpServletRequest request) {
        if (isBrowser(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        else {
            if (clientRepository.existsById(clientNumber)) {
                clientRepository.deleteById(clientNumber);
                return ResponseEntity.noContent().build();
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
    }

    @GetMapping("/max-client-number")
    public ResponseEntity<Long> getMaxClientNumber() {
        Long maxClientNumber = clientRepository.findMaxClientNumber()
                .orElse(0L);
        return ResponseEntity.ok(maxClientNumber);
    }
}
