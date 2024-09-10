package pl.bpwesley.TourOperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bpwesley.TourOperator.entity.Client;
import pl.bpwesley.TourOperator.repository.ClientRepository;

import jakarta.servlet.http.HttpServletRequest;
import pl.bpwesley.TourOperator.service.ClientService;

import java.util.List;

@RestController // klasa jest kontrolerem REST, ktory obsluguje zapytania HTTP i zwraca dane
@RequestMapping("/api/client") // bazowy URL dla wszystkich metod w tym kontrolerze
public class ClientApiController {
    private final ClientRepository clientRepository;
    private final ClientService clientService;

    @Autowired // automatyczne wstrzykniecie instancji klasy do kontrolera
    public ClientApiController(ClientRepository clientRepository, ClientService clientService) {
        this.clientRepository = clientRepository;
        this.clientService = clientService;
    }

    // Metoda sprawdzajaca czy request pochodzi z przegladarki
    private boolean isBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null && userAgent.contains("Mozilla");
    }

    @GetMapping("/")
    public ResponseEntity<List<Client>> getAllClients(HttpServletRequest request) {
        if (isBrowser(request)) {
            // Zwroc status 403 FORBIDDEN
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
            // Zwroc status 403 FORBIDDEN
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
            // Zwroc status 403 FORBIDDEN
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
        return clientRepository.findByNameIgnoreCaseAndAccent(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    @PostMapping("/add")
    public ResponseEntity<Client> addClient(@RequestBody Client newClient,
                                            HttpServletRequest request) {
        // @RequestBody - mapowanie danych przesyłanych w ciele (body) żądania HTTP do obiektu w aplikajci;
        // automatyczne przekstrzalcenie obiektu JSON na obiekt Java
        if (isBrowser(request)) {
            // Zwroc status 403 FORBIDDEN
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Sprawdzenie czy klient o tej samej nazwie juz istnieje
        if (clientRepository.findByNameIgnoreCaseAndAccent(newClient.getName()).isPresent()) {
            // Jesli tak, zwracamy status 409 CONFLICT
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        try {
            // Zapisz nowego klienta do bazy i zwroc status 201 CREATED oraz zapisanego klienta
            Client savedClient = clientRepository.save(newClient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
        }
        catch (Exception e) {
            // w przypadku wyjatku zwroc status 400 BAD REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{clientNumber}")
    public ResponseEntity<Void> deleteClient(@PathVariable("clientNumber") Long clientNumber,
                                             HttpServletRequest request) {
        if (isBrowser(request)) {
            // Zwroc status 403 FORBIDDEN
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Sprawdzenie czy klient o podanym numerze istnieje w bazie
        if (clientRepository.existsById(clientNumber)) {
            // Jesli tak, usun go i zwroc status 204 NO CONTENT
            clientRepository.deleteById(clientNumber);
            return ResponseEntity.noContent().build();
        }
        else {
            // Jesli nie, zwroc status 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/update/{clientNumber}")
    public ResponseEntity<Client> updateClient(@PathVariable("clientNumber") Long clientNumber,
                                               @RequestBody Client updatedClient,
                                               HttpServletRequest request) {
        if (isBrowser(request)) {
            // Zwroc status 403 FORBIDDEN
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Sprawdzenie czy klient o danym numerze istnieje
        Client existingClient = clientRepository.findByClientNumber(clientNumber).orElse(null);
        if (existingClient == null) {
            // Jesli nie, zwroc status 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Sprawdzenie czy klient o podanej nazwie juz istnieje w bazie
        if (clientRepository.findByNameIgnoreCaseAndAccent(updatedClient.getName())
            .filter(client -> !client.getClientNumber().equals(clientNumber))
            .isPresent()) {
            // Jesli tak, zwroc status 409 CONFLICT
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Zaktualizuj dane i zapisz klienta w bazie
        Client updated = clientService.updateClientData(existingClient, updatedClient);

        // Zwroc go z kodem 200 OK
        return ResponseEntity.ok(updated);
}





        // Znajdz istniejacego klienta po numerze i zaktualizuj dane
//        return clientRepository.findByClientNumber(clientNumber)
//                .map(existingClient -> {
// Sprawdzenie czy klient o podanej nazwie juz istnieje w bazie
//                    if (clientRepository.findByNameIgnoreCaseAndAccent(updatedClient.getName())
//                            .filter(client -> !client.getClientNumber().equals(clientNumber))
//                            .isPresent()) {
//                        // Jesli tak, zwroc status 409 CONFLICT
//                        return ResponseEntity.status(HttpStatus.CONFLICT).build();
//                    }
//
//                    existingClient.setName(updatedClient.getName());
//                    existingClient.setAlias(updatedClient.getAlias());
//                    existingClient.setStreet(updatedClient.getStreet());
//                    existingClient.setNumber(updatedClient.getNumber());
//                    existingClient.setPostCode(updatedClient.getPostCode());
//                    existingClient.setCity(updatedClient.getCity());
//                    existingClient.setPickupLocation(updatedClient.getPickupLocation());
//
//                    // Zapisz klienta w bazie i zwroc go z kodem 200 OK
//                    Client savedClient = clientRepository.save(existingClient);
//                    return ResponseEntity.ok(savedClient);
//                })
//                // Jesli klient o podanym numerze nie istnieje, zwroc 404 NOT FOUND
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }

    @GetMapping("/max-client-number")
    public ResponseEntity<Long> getMaxClientNumber() {
        Long maxClientNumber = clientRepository.findMaxClientNumber()
                .orElse(0L);
        return ResponseEntity.ok(maxClientNumber);
    }
}
