package pl.bpwesley.TourOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.bpwesley.TourOperator.controller.ClientApiController;
import pl.bpwesley.TourOperator.dto.ClientDTO;
import pl.bpwesley.TourOperator.mapper.ClientMapper;
import pl.bpwesley.TourOperator.service.ClientService;
import pl.bpwesley.TourOperator.repository.ClientRepository;

import java.util.List;

import static org.mockito.Mockito.doReturn;

@WebMvcTest(ClientApiController.class)
public class ClientApiTests {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientApiController clientApiController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientApiController).build();
    }

    @Test
    public void testGetAllClients() throws Exception {
        ClientDTO clientDTO = new ClientDTO(1L, "Test Client", null, "ul. Testowa", "1", "00-000", "Testowo", null);
        doReturn(List.of(clientDTO)).when(clientService).getClientList();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/client/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Test Client"));
    }
}
