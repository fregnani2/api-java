package com.example.api.repository;

import com.example.api.entity.Client;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should return a client by account number")
    void getClientByAccountNumber() {
        Client client = this.createClient();
        Optional<Client> result = Optional.ofNullable(clientRepository.getClientByAccountNumber(client.getAccountNumber()));
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not return a client by account number that doesn't exists")
    void getClientByAccountNumberWhenClientNotFound() {
        Optional<Client> result = Optional.ofNullable(clientRepository.getClientByAccountNumber(123456L));
        assertThat(result.isEmpty()).isTrue();
    }


    private Client createClient() {
        Client client = new Client();
        client.setAccountNumber(123456L);
        client.setBalance(1000.0);
        entityManager.persist(client);
        return client;
    }
}