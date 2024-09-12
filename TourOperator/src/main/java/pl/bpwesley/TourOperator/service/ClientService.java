package pl.bpwesley.TourOperator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.dto.ClientDTO;
import pl.bpwesley.TourOperator.entity.Client;
import pl.bpwesley.TourOperator.repository.ClientRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public ClientDTO addClient(ClientDTO clientDTO) {
        Client client = toEntity(clientDTO);
        // INSERT INTO
        Client savedClient = clientRepository.save(client);
        return toDTO(savedClient);
    }

    public void deleteClient(Long clientNumber) {
        // DELETE FROM
        clientRepository.deleteById(clientNumber);
    }

    public ClientDTO updateClientData(ClientDTO updatedClientDTO) {
        // Znajdz klienta lub zwroc wyjatek
        Client existingClient = clientRepository.findById(updatedClientDTO.getClientNumber())
                .orElseThrow(() -> new IllegalStateException("Blad przy zapisie"));

        // Zaktualizuj dane i zapisz klienta w bazie
        existingClient.setName(updatedClientDTO.getName());
        existingClient.setAlias(updatedClientDTO.getAlias());
        existingClient.setStreet(updatedClientDTO.getStreet());
        existingClient.setNumber(updatedClientDTO.getNumber());
        existingClient.setPostCode(updatedClientDTO.getPostCode());
        existingClient.setCity(updatedClientDTO.getCity());
        existingClient.setPickupLocation(updatedClientDTO.getPickupLocation());
        // UPDATE SET
        Client updatedClient = clientRepository.save(existingClient);
        return toDTO(updatedClient);
    }
    public List<ClientDTO> getClientList() {
        // SELECT *
        List<Client> clientList = clientRepository.findAllByOrderByClientNumberAsc();
        return clientList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // API
    public Optional<ClientDTO> getClientByNumber(Long clientNumber) {
        // SELECT WHERE id
        return clientRepository.findById(clientNumber)
                .map(this::toDTO);
    }

    public Optional<ClientDTO> getClientByName(String name) {
        // SELECT WHERE name
        return clientRepository.findByNameIgnoreCaseAndAccent(name)
                .map(this::toDTO);
    }

    public Long getMaxClientNumber() {
        return clientRepository.findMaxClientNumber().orElse(0L);
    }

    // DTO
    private ClientDTO toDTO(Client client) {
        return new ClientDTO(
                client.getClientNumber(),
                client.getName(),
                client.getAlias(),
                client.getStreet(),
                client.getNumber(),
                client.getPostCode(),
                client.getCity(),
                client.getPickupLocation()
        );
    }

    public Client toEntity(ClientDTO dto) {
        Client client = new Client();
        client.setClientNumber(dto.getClientNumber());
        client.setName(dto.getName());
        client.setAlias(dto.getAlias());
        client.setStreet(dto.getStreet());
        client.setNumber(dto.getNumber());
        client.setPostCode(dto.getPostCode());
        client.setCity(dto.getCity());
        client.setPickupLocation(dto.getPickupLocation());
        return client;
    }
}
