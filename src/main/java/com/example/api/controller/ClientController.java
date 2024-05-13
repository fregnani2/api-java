package com.example.api.controller;

import com.example.api.entity.Client;
import com.example.api.entity.Transaction;
import com.example.api.service.ClientService;
import com.example.api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Client> create(@RequestBody Client client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(client));
    }


    @GetMapping
    public ResponseEntity<List<Client>> getAll() {
        return ResponseEntity.ok().body(clientService.getAll());
    }

    @GetMapping(value = "/account/{accountNumber}")
    public ResponseEntity<Client> getByAccountNumber(@PathVariable Long accountNumber) {
        return ResponseEntity.ok().body(clientService.getByAccountNumber(accountNumber));
    }

    @PostMapping(value = "/transfer")
    public ResponseEntity<Void> makeTransfer(@RequestBody Transaction transaction) {
        transactionService.makeTransfer(transaction.getFromAccountNumber(), transaction.getToAccountNumber(), transaction.getValue());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/transactions/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long accountNumber) {
        return ResponseEntity.ok().body(transactionService.getTransactionByAccountNumber(accountNumber));
    }

}


/*
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Client> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.getById(id));
    }
      @PutMapping(value = "/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client obj) {
        obj.setId(id);
        return ResponseEntity.ok().body(service.update(obj));
    }*/