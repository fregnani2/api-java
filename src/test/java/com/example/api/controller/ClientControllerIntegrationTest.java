package com.example.api.controller;

import com.example.api.entity.Client;
import com.example.api.repository.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the ClientController
 * @SpringBootTest starts the whole Spring application context
 * @AutoConfigureMockMvc Annotation that can be applied to a test class to enable and configure auto-configuration of MockMvc.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ClientControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    MockMvc mockMvc;

    private Long client1Id;

    /**
     * Create 2 clients before each test
     */
    @BeforeEach
    void setup() {
        Client client = new Client();
        client.setName("Bruno");
        client.setAccountNumber(220);
        client.setBalance(1000.0);

        Client client2 = new Client();
        client2.setName("Marcos");
        client2.setAccountNumber(121);
        client2.setBalance(1000.0);


        clientRepository.save(client);
        clientRepository.save(client2);

        client1Id = client.getId();
    }

    /**
     * Delete all clients after each test
     */
    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
    }

    /**
     * MockMvc is used to send HTTP Request to the API and inspect the responses, within the test environment
     */
    @Test
    @DisplayName("Should create a client")
    public void createclient() throws Exception {
        mockMvc.perform(post("/clients/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"name\": \"Bruno\",\n" +
                        "    \"accountNumber\": 123,\n" +
                        "    \"balance\": 1000.0\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value(123));
    }

    @Test
    @DisplayName("Should not create a client with an existing account number")
    public void createclientWithExistingAccountNumber() throws Exception {
        mockMvc.perform(post("/clients/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"Bruno\",\n" +
                                "    \"accountNumber\": 220,\n" +
                                "    \"balance\": 1000.0\n" +
                                "}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Account number already exists"));
    }

    @Test
    @DisplayName("Should return a client by an account number")
    public void getClientByAccountNumber() throws Exception {

        mockMvc.perform(get("/clients/v1/220"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(220));

    }

    @Test
    @DisplayName("Should not return a client by an account number that doesn't exist")
    public void getClientByExistingAccountNumber() throws Exception {
        mockMvc.perform(get("/clients/v1/123"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Should make a transfer")
    public void makeTransfer() throws Exception {
        mockMvc.perform(post("/clients/v1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"fromAccountNumber\": 220,\n" +
                        "    \"toAccountNumber\": 121,\n" +
                        "    \"value\": 100.0\n" +
                        "}"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should not make a transfer with an amount greater than 100")
    public void makeTransferWithAmountGreaterThan100() throws Exception {
        mockMvc.perform(post("/clients/v1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"fromAccountNumber\": 220,\n" +
                        "    \"toAccountNumber\": 121,\n" +
                        "    \"value\": 101.0\n" +
                        "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Max amount to transfer is 100"));
    }

    @Test
    @DisplayName("Should not make a transfer with an amount less than 1")
    public void makeTransferWithAmountLessThan1() throws Exception {
        mockMvc.perform(post("/clients/v1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"fromAccountNumber\": 220,\n" +
                        "    \"toAccountNumber\": 121,\n" +
                        "    \"value\": 0.0\n" +
                        "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Minimum amount to transfer is 1"));
    }

    @Test
    @DisplayName("Should make one successful transfer and one failed transfer and return the transactions")
    public void getTransactions() throws Exception {
        mockMvc.perform(post("/clients/v1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"fromAccountNumber\": 220,\n" +
                        "    \"toAccountNumber\": 121,\n" +
                        "    \"value\": 100.0\n" +
                        "}"))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/clients/v1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"fromAccountNumber\": 220,\n" +
                        "    \"toAccountNumber\": 121,\n" +
                        "    \"value\": 1000.0\n" +
                        "}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/clients/v1/transactions/220"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("FAILED"));

        mockMvc.perform(get("/clients/v1/transactions/220"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].status").value("SUCCESS"));
    }


    @Test
    @DisplayName("Should not create a client with a null name")
    public void createClientWithNullName() throws Exception {
        mockMvc.perform(post("/clients/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"name\": null,\n" +
                        "    \"accountNumber\": 123,\n" +
                        "    \"balance\": 1000.0\n" +
                        "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Name cannot be null or empty"));
    }


    @Test
    @DisplayName("Should not create a client with a negative balance")
    public void createClientWithNegativeBalance() throws Exception {
        mockMvc.perform(post("/clients/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"name\": \"Bruno\",\n" +
                        "    \"accountNumber\": 123,\n" +
                        "    \"balance\": -1000.0\n" +
                        "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Balance cannot be negative or null"));
    }

    //V2 Tests
    @Test
    @DisplayName("Should return a page of transactions")
    public void getTransactionsV2() throws Exception {
        mockMvc.perform(get("/clients/v2/transactions?fromAccountNumber=220&page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @DisplayName("Should update a client")
    public void updateClient() throws Exception{
        mockMvc.perform(put("/clients/v2/"+client1Id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"name\": \"Bruno\",\n" +
                        "    \"accountNumber\": 555,\n" +
                        "    \"balance\": 1000.0\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(555));
    }

}