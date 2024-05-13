package com.example.api.service;

import com.example.api.entity.Client;
import com.example.api.repository.ClientRepository;
import com.example.api.repository.TransactionRepository;
import com.example.api.service.exception.DuplicateAccount;
import com.example.api.service.exception.EntityNotFound;
import com.example.api.service.exception.InsufficientBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Autowired
    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should not create a client with an existing account number")
    void create() {
        Client client1 = new Client(1L,"Bruno", 324L,1000.0);
        Client client2 = new Client(2L,"Marcos", 324L,5000.0);

        when(clientRepository.getClientByAccountNumber(324L)).thenReturn(client1);
        assertThrows(DuplicateAccount.class, () -> clientService.create(client2));
    }

    @Test
    @DisplayName("Should return a client by account number")
    void getByAccountNumber() {
        Client client = new Client(1L,"Bruno", 324L,1000.0);
        when(clientRepository.getClientByAccountNumber(324L)).thenReturn(client);
        assertEquals(client, clientService.getByAccountNumber(324L));
    }

    @Test
    @DisplayName("Should throw an exception when trying to get a client by account number that doesn't exists")
    void getByAccountNumberWhenClientNotFound() {
        when(clientRepository.getClientByAccountNumber(324L)).thenReturn(null);
        assertThrows(EntityNotFound.class, () -> clientService.getByAccountNumber(324L));
    }
}