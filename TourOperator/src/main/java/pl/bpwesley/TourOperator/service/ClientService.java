package pl.bpwesley.TourOperator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.dto.ClientDTO;
import pl.bpwesley.TourOperator.exception.ClientAlreadyExistsException;
import pl.bpwesley.TourOperator.exception.ClientNotFoundException;
import pl.bpwesley.TourOperator.mapper.ClientMapper;
import pl.bpwesley.TourOperator.entity.Client;
import pl.bpwesley.TourOperator.repository.ClientRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper = ClientMapper.INSTANCE;

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
        // Sprawdz czy klient o takiej nazwie istnieje
        if (clientExistsByName(clientDTO.getName())) {
            throw new ClientAlreadyExistsException("Klient o nazwie " + clientDTO.getName() + " już istnieje");
        }
        Client client = clientMapper.clientDTOToClient(clientDTO);
        // INSERT INTO
        return clientMapper.clientToClientDTO(clientRepository.save(client));
    }

    public void deleteClient(Long clientNumber) {
        // Sprawdz czy klient o takim numerze istnieje
        if (!clientExistsByClientNumber(clientNumber)) {
            throw new ClientNotFoundException("Klient o numerze " + clientNumber + " nie istnieje");
        }
        // DELETE FROM
        clientRepository.deleteById(clientNumber);
    }

    public ClientDTO updateClientData(ClientDTO updatedClientDTO) {
        Long clientNumber = updatedClientDTO.getClientNumber();

        // Sprawdz czy klient o takim numerze istnieje
        clientRepository.findById(clientNumber)
                .orElseThrow(() -> new ClientNotFoundException("Klient o numerze " + clientNumber + " nie istnieje"));
        Client existingClient;

        String name = updatedClientDTO.getName();
        if (clientExistsByName(name)) {
            // Sprawdz unikalnosc nowej nazwy
            Client clientWithSameName = clientRepository.findByNameIgnoreCaseAndAccent(name).orElse(null);
            if (clientWithSameName != null && !clientWithSameName.getClientNumber().equals(clientNumber)) {
                throw new ClientAlreadyExistsException("Klient o nazwie " + name + " już istnieje");
            }
        }

        // Zaktualizuj dane i zapisz klienta w bazie
        existingClient = clientMapper.clientDTOToClient(updatedClientDTO);
        // UPDATE SET
        return clientMapper.clientToClientDTO(clientRepository.save(existingClient));
    }
    public List<ClientDTO> getClientList() {
        // SELECT *
        return clientRepository.findAllByOrderByClientNumberAsc().stream()
                .map(clientMapper::clientToClientDTO)
                .collect(Collectors.toList());
    }

    // API
    public Optional<ClientDTO> getClientByNumber(Long clientNumber) {
        // SELECT WHERE id
        return clientRepository.findById(clientNumber)
                .map(clientMapper::clientToClientDTO);
    }

    public Optional<ClientDTO> getClientByName(String name) {
        // SELECT WHERE name
        return clientRepository.findByNameIgnoreCaseAndAccent(name)
                .map(clientMapper::clientToClientDTO);
    }

    public Long getMaxClientNumber() {
        return clientRepository.findMaxClientNumber().orElse(0L);
    }
}