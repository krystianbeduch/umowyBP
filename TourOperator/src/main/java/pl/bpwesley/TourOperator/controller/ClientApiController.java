package pl.bpwesley.TourOperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bpwesley.TourOperator.dto.ClientDTO;
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
    public ResponseEntity<List<ClientDTO>> getAllClients(HttpServletRequest request) {
        if (isBrowser(request)) {
            // Zwroc status 403 FORBIDDEN
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(clientService.getClientList());
    }

    @GetMapping("/{clientNumber}") // metoda odpowiada na zapytania GET na podany mapping
    public ResponseEntity<ClientDTO> getClientByNumber(@PathVariable("clientNumber") Long clientNumber,
                                                    HttpServletRequest request) {
//    @PathVariable - {clientNumber} z URL jest przekazywana do metody jako argument
// Blokada spowodowala problem z auto uzupelnianiem formularza do usuwania
//        if (isBrowser(request)) {
        // Zwroc status 403 FORBIDDEN
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
        return clientService.getClientByNumber(clientNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ClientDTO> getClientByName(@PathVariable("name") String name,
                                                  HttpServletRequest request) {
//        if (isBrowser(request)) {
        // Zwroc status 403 FORBIDDEN
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
        return clientService.getClientByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @PostMapping("/add")
    public ResponseEntity<ClientDTO> addClient(@RequestBody ClientDTO newClientDTO,
                                            HttpServletRequest request) {
        // @RequestBody - mapowanie danych przesyłanych w ciele (body) żądania HTTP do obiektu w aplikajci;
        // automatyczne przekstrzalcenie obiektu JSON na obiekt Java
        if (isBrowser(request)) {
            // Zwroc status 403 FORBIDDEN
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Sprawdzenie czy klient o tej samej nazwie juz istnieje
        if (clientService.clientExistsByName(newClientDTO.getName())) {
            // Jesli tak, zwracamy status 409 CONFLICT
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        try {
            // Zapisz nowego klienta do bazy i zwroc status 201 CREATED oraz zapisanego klienta
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    clientService.addClient(newClientDTO));
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
        if (clientService.clientExistsByClientNumber(clientNumber)) {
            // Jesli tak, usun go i zwroc status 204 NO CONTENT
            clientService.deleteClient(clientNumber);
            return ResponseEntity.noContent().build();
        } else {
            // Jesli nie, zwroc status 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/update/{clientNumber}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable("clientNumber") Long clientNumber,
                                               @RequestBody ClientDTO updatedClientDTO,
                                               HttpServletRequest request) {
        if (isBrowser(request)) {
            // Zwroc status 403 FORBIDDEN
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Sprawdzenie czy klient o danym numerze istnieje
        if (!clientService.clientExistsByClientNumber(clientNumber)) {
            // Jesli nie, zwroc status 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Sprawdz czy nazwa jest unikalna dla innych klientow
        if (clientService.getClientByName(updatedClientDTO.getName())
                .filter(client -> !client.getClientNumber().equals(clientNumber))
                .isPresent()) {
            // Jesli istnieje inny klient z taka nazwa, zwroc status 409 CONFLICT
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        updatedClientDTO.setClientNumber(clientNumber);

        // Zaktualizuj dane, zapisz klienta w bazie i zwroc go z kodem 200 OK
        return ResponseEntity.ok(clientService.updateClientData(updatedClientDTO));
    }

    @GetMapping("/max-client-number")
    public ResponseEntity<Long> getMaxClientNumber() {
        return ResponseEntity.ok(clientService.getMaxClientNumber());
    }
}
