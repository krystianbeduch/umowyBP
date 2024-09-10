package pl.bpwesley.TourOperator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.entity.Client;
import pl.bpwesley.TourOperator.repository.ClientRepository;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client updateClientData(Client existingClient, Client updatedClient) {
        existingClient.setName(updatedClient.getName());
        existingClient.setAlias(updatedClient.getAlias());
        existingClient.setStreet(updatedClient.getStreet());
        existingClient.setNumber(updatedClient.getNumber());
        existingClient.setPostCode(updatedClient.getPostCode());
        existingClient.setCity(updatedClient.getCity());
        existingClient.setPickupLocation(updatedClient.getPickupLocation());

        return clientRepository.save(existingClient);
    }
}
