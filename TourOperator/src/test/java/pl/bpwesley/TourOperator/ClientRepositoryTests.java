package pl.bpwesley.TourOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import pl.bpwesley.TourOperator.model.Client;
import pl.bpwesley.TourOperator.model.PickupLocation;
import pl.bpwesley.TourOperator.repository.ClientRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    public void testSaveClient() {
        // Arrange
        Client client = new Client("Test Client", null, "ul. Testowa", "1", "00-000", "Testowo",
                new PickupLocation());
//        client.setClientNumber(50L);

        // Act
        Client savedClient = clientRepository.save(client);

        // Assert
        assertNotNull(savedClient.getClientNumber());

    }
}
