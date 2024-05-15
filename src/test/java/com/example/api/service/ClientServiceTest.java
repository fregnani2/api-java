package com.example.api.service;

import com.example.api.entity.Client;
import com.example.api.repository.ClientRepository;
import com.example.api.repository.TransactionRepository;
import com.example.api.service.V2.ClientServiceV2;
import com.example.api.service.exception.DuplicateAccount;
import com.example.api.service.exception.EntityNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the ClientService
 */
@SpringBootTest()
class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @Autowired
    @InjectMocks
    private ClientService clientService;


    @Autowired
    @InjectMocks
    private ClientServiceV2 clientServiceV2;


    /**
     * Setup before each test
     * Initialize @Mock objects before each test
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should not create a client with an existing account number")
    void create() {
        Client client1 = new Client(1L,"Bruno", 324,1000.0);
        Client client2 = new Client(2L,"Marcos", 324,5000.0);

        when(clientRepository.getClientByAccountNumber(324)).thenReturn(client1);
        assertThrows(DuplicateAccount.class, () -> clientService.create(client2));
    }

    @Test
    @DisplayName("Should return a client by account number")
    void getByAccountNumber() {
        Client client = new Client(1L,"Bruno", 324,1000.0);
        when(clientRepository.getClientByAccountNumber(324)).thenReturn(client);
        assertEquals(client, clientService.getByAccountNumber(324));
    }

    @Test
    @DisplayName("Should throw an exception when trying to get a client by account number that doesn't exists")
    void getByAccountNumberWhenClientNotFound() {
        when(clientRepository.getClientByAccountNumber(324)).thenReturn(null);
        assertThrows(EntityNotFound.class, () -> clientService.getByAccountNumber(324));
    }


    @Test
    @DisplayName("Should update a client")
    void updateClient() {
        Optional<Client> Opt = Optional.of(new Client(1L,"Bruno", 324,1000.0));
        Client newClient = new Client(2L,"Marcos", 222,2000.0);
        Client client = Opt.get();

        clientServiceV2.updateClient(Opt, newClient);
        assertEquals(2000.0, client.getBalance());
        assertEquals("Marcos", client.getName());
        assertEquals(222, client.getAccountNumber());

    }

}