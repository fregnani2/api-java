package com.example.api.controller;

import com.example.api.entity.Client;
import com.example.api.entity.Transaction;
import com.example.api.service.ClientService;
import com.example.api.service.TransactionService;
import com.example.api.service.V2.ClientServiceV2;
import com.example.api.service.V2.TransactionServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Client entity and Transaction entity
 * @RestController annotation is used to create RESTful web services using Spring MVC
 * @RequestMapping annotation is used to map web requests to Spring Controller methods
 */
@RestController
@RequestMapping(value = "/clients")
public class EntityController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientServiceV2 clientServiceV2;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionServiceV2 transactionServiceV2;

    /**
     * Create a new client
     * @param client client object
     */
    @PostMapping(value = "/v1")
    public ResponseEntity<Client> create(@RequestBody Client client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(client));
    }

    /**
     * Get all clients
     */
    @GetMapping(value = "/v1")
    public ResponseEntity<List<Client>> getAll() {
        return ResponseEntity.ok().body(clientService.getAll());
    }

    /**
     * Get a client by account number
     * @param accountNumber account number
     */
    @GetMapping(value = "/v1/{accountNumber}")
    public ResponseEntity<Client> getByAccountNumber(@PathVariable Integer accountNumber) {
        return ResponseEntity.ok().body(clientService.getByAccountNumber(accountNumber));
    }


    /**
     * Make a transfer between two accounts
     * @param transaction transaction object
     */
    @PostMapping(value = "/v1/transfer")
    public ResponseEntity<Void> makeTransfer(@RequestBody Transaction transaction) {
        transactionService.makeTransfer(transaction.getFromAccountNumber(), transaction.getToAccountNumber(), transaction.getValue());
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all transactions by account number
     * @param accountNumber account number
     */
    @GetMapping(value = "/v1/transactions/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Integer accountNumber) {
        return ResponseEntity.ok().body(transactionService.getTransactionByAccountNumber(accountNumber));
    }


    //V2 endpoints

    /**
     * Update a client
     * @param id client id
     * @param client client object
     */
    @PutMapping(value = "/v2/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client client) {
        client.setId(id);
        return ResponseEntity.ok().body(clientServiceV2.update(client));
    }

    /**
     * Get Transactions by account number with pagination
     * @param fromAccountNumber account number
     * @param pageable pagination
     */
    @GetMapping(value = "/v2/transactions")
    public ResponseEntity<Page<Transaction>> makeTransferV2(@RequestParam Integer fromAccountNumber, Pageable pageable) {
        Page<Transaction> transactions = transactionServiceV2.getTransactionByAccountNumber(fromAccountNumber, pageable);
        return ResponseEntity.ok().body(transactions);
    }

}