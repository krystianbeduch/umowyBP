package pl.bpwesley.TourOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import pl.bpwesley.TourOperator.dto.ClientDTO;
import pl.bpwesley.TourOperator.entity.Client;
import pl.bpwesley.TourOperator.entity.PickupLocation;
import pl.bpwesley.TourOperator.exception.ClientAlreadyExistsException;
import pl.bpwesley.TourOperator.mapper.ClientMapper;
import pl.bpwesley.TourOperator.repository.ClientRepository;
import pl.bpwesley.TourOperator.service.ClientService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClientServiceTests {

    // mockujemy zaleznosci dzieki czemu nie korzystamy z rzeczywistej bazy danych ani mapera
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    // wstrzykujemy mocki do instancji ktorej bedziemy uzywac w testach
    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        // inicjujemy mocki przed kazdym testem
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testAddClient() {
        // Tworzymy obiekt ktory bedzie przekazywany do addClient
        ClientDTO clientDTO = new ClientDTO(null, "Test Client", null, "ul. Testowa", "1", "00-000", "Testowo",
                new PickupLocation());

        // Tworzymy obiekt ktory odpowiada encji
        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setAlias(clientDTO.getAlias());
        client.setStreet(clientDTO.getStreet());
        client.setNumber(clientDTO.getNumber());
        client.setPostCode(clientDTO.getPostCode());
        client.setCity(clientDTO.getCity());
        client.setPickupLocation(clientDTO.getPickupLocation());

        // Definiujemy zachowanie mocka mappera, ktory na postawie przekaznego DTO zwroci encji Client
//        when(clientMapper.clientDTOToClient(any(ClientDTO.class)))
//                .thenReturn(client);
        doReturn(client).when(clientMapper).clientDTOToClient(any(ClientDTO.class));

        // Na podstawie encji Client, zwroc zapisany obiekt
//        when(clientRepository.save(any(Client.class)))
//                .thenReturn(client);
        doReturn(client).when(clientRepository).save(any(Client.class));

        ClientDTO savedClientDTO = clientService.addClient(clientDTO);

        assertNotNull(savedClientDTO);
        assertEquals(clientDTO.getName(), savedClientDTO.getName());
        assertEquals(clientDTO.getAlias(), savedClientDTO.getAlias());
        assertEquals(clientDTO.getStreet(), savedClientDTO.getStreet());
        assertEquals(clientDTO.getNumber(), savedClientDTO.getNumber());
        assertEquals(clientDTO.getPostCode(), savedClientDTO.getPostCode());
        assertEquals(clientDTO.getCity(), savedClientDTO.getCity());
        assertEquals(clientDTO.getPickupLocation(), savedClientDTO.getPickupLocation());
    }

    @Test
    public void testDeleteClient() {
        // Arrange
        Long clientNumber = 1L;

        // Ustawienie mocka aby symulowal istnieje klienta
        when(clientRepository.existsById(clientNumber)).thenReturn(true);

        // Act
        clientService.deleteClient(clientNumber);

        // Assert
        // Weryfikacja, czy metoda deleteById zostala wywolana
        verify(clientRepository).deleteById(clientNumber);
    }

    @Test
    public void testUpdateClient() {
        // Arrange
        Long clientNumber = 1L;
        ClientDTO clientDTO = new ClientDTO(clientNumber ,"Test Client", null, "ul. Testowa", "1", "00-000", "Testowo",
                new PickupLocation());

        Client existingClient = new Client();
        existingClient.setClientNumber(clientNumber);
        existingClient.setName("Old Client");
        existingClient.setAlias("Old Alias");
        existingClient.setStreet("Old Street");
        existingClient.setNumber("1");
        existingClient.setPostCode("00-001");
        existingClient.setCity("Old City");
        existingClient.setPickupLocation(new PickupLocation());

        Client updatedClient = new Client();
        updatedClient.setClientNumber(clientNumber);
        updatedClient.setName(clientDTO.getName());
        updatedClient.setAlias(clientDTO.getAlias());
        updatedClient.setStreet(clientDTO.getStreet());
        updatedClient.setNumber(clientDTO.getNumber());
        updatedClient.setCity(clientDTO.getCity());
        updatedClient.setPostCode(clientDTO.getPostCode());
        updatedClient.setPickupLocation(clientDTO.getPickupLocation());

        // Mockowanie metod mapera i repo
        when(clientRepository.findById(clientNumber))
                .thenReturn(java.util.Optional.of(existingClient));

        when(clientMapper.clientDTOToClient(any(ClientDTO.class)))
                .thenReturn(updatedClient);

        when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);

        ClientDTO resultClientDTO = clientService.updateClientData(clientDTO);
        assertNotNull(resultClientDTO);
        assertEquals(clientDTO.getName(), resultClientDTO.getName());
        assertEquals(clientDTO.getAlias(), resultClientDTO.getAlias());
        assertEquals(clientDTO.getStreet(), resultClientDTO.getStreet());
        assertEquals(clientDTO.getNumber(), resultClientDTO.getNumber());
        assertEquals(clientDTO.getPostCode(), resultClientDTO.getPostCode());
        assertEquals(clientDTO.getCity(), resultClientDTO.getCity());
        assertEquals(clientDTO.getPickupLocation(), resultClientDTO.getPickupLocation());
    }

    @Test
    public void testAddClientWithExistingName() {
        // Tworzymy obiekt ktory bedzie przekazywany do addClient
        ClientDTO clientDTO = new ClientDTO(null, "Test Client", null, "ul. Testowa", "1", "00-000", "Testowo", null);
//        doReturn(true).when(clientService).clientExistsByName(clientDTO.getName());
        doReturn(true).when(clientRepository).findByNameIgnoreCaseAndAccent(clientDTO.getName());
//        when(clientService.clientExistsByName(clientDTO.getName())).thenThrow(new ClientAlreadyExistsException("Klient o nazwie Test Client już istnieje"));
//        when(clientService.clientExistsByName(clientDTO.getName()))
//                .thenReturn(true);
//        doThrow(new ClientAlreadyExistsException("Klient o nazwie Test Client już istnieje"))
//                .when(clientService).clientExistsByName("Test Client");
//
//
//
//        Exception exception = assertThrows(ClientAlreadyExistsException.class, () -> {
//            clientService.addClient(clientDTO);
//        });
        // Symulujemy, że klient o podanej nazwie już istnieje
//        when(clientService.clientExistsByName("Test Client"))
//                .thenThrow(new ClientAlreadyExistsException("Klient o nazwie Test Client już istnieje"));

        // Wywołujemy metodę i sprawdzamy, czy został rzucony wyjątek
        Exception exception = assertThrows(ClientAlreadyExistsException.class, () -> {
            clientService.addClient(clientDTO);
        });

        assertEquals("Klient o nazwie Test Client już istnieje", exception.getMessage());
    }

}
