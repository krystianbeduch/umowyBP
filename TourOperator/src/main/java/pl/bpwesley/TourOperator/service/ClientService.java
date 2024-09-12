package pl.bpwesley.TourOperator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.entity.Client;
import pl.bpwesley.TourOperator.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public boolean clientExistsByName(String name) {
        return clientRepository.findByNameIgnoreCaseAndAccent(name).isPresent();
    }

    public boolean clientExistsByClientNumber(Long clientNumber) {
        return clientRepository.existsById(clientNumber);
    }

    public Client addClient(Client client) {
        // INSERT INTO
        return clientRepository.save(client);
    }

    public void deleteClient(Long clientNumber) {
        // DELETE FROM
        clientRepository.deleteById(clientNumber);
    }

    public Client updateClientData(Client updatedClient) {
        // Znajdz klienta lub zwroc wyjatek
        Client existingClient = clientRepository.findById(updatedClient.getClientNumber())
                .orElseThrow(() -> new IllegalStateException("Blad przy zapisie"));

        // Zaktualizuj dane i zapisz klienta w bazie
        existingClient.setName(updatedClient.getName());
        existingClient.setAlias(updatedClient.getAlias());
        existingClient.setStreet(updatedClient.getStreet());
        existingClient.setNumber(updatedClient.getNumber());
        existingClient.setPostCode(updatedClient.getPostCode());
        existingClient.setCity(updatedClient.getCity());
        existingClient.setPickupLocation(updatedClient.getPickupLocation());
        // UPDATE SET
        return clientRepository.save(existingClient);
    }
    public List<Client> getClientList() {
        // SELECT *
        return clientRepository.findAllByOrderByClientNumberAsc();
    }

    // API
    public Optional<Client> getClientByNumber(Long clientNumber) {
        // SELECT WHERE id
        return clientRepository.findById(clientNumber);
    }

    public Optional<Client> getClientByName(String name) {
        // SELECT WHERE name
        return clientRepository.findByNameIgnoreCaseAndAccent(name);
    }

    public Long getMaxClientNumber() {
        return clientRepository.findMaxClientNumber().orElse(0L);
    }
}