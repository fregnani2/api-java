package com.example.api.service;

import com.example.api.entity.Client;
import com.example.api.entity.Transaction;
import com.example.api.repository.ClientRepository;
import com.example.api.repository.TransactionRepository;
import com.example.api.service.exception.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private final ConcurrentHashMap<Long, Lock> locks = new ConcurrentHashMap<>();

    public Client create(Client obj) {
        if (clientRepository.getClientByAccountNumber(obj.getAccountNumber()) != null) {
            throw new DuplicateAccount("Account number already exists");
        }
        return clientRepository.save(obj);
    }

    public Client getByAccountNumber(Long accountNumber) {
        Optional<Client> obj = Optional.ofNullable(clientRepository.getClientByAccountNumber(accountNumber));
        if (obj.isEmpty()) {
            throw new EntityNotFound("Client of account number " + accountNumber + " not found");
        }
        return clientRepository.getClientByAccountNumber(accountNumber);

    }

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

/*
    public void makeTransfer(Long fromAccountNumber, Long toAccountNumber, Double value) {
        Lock fromLock = locks.computeIfAbsent(fromAccountNumber, k -> new ReentrantLock());
        Lock toLock = locks.computeIfAbsent(toAccountNumber, k -> new ReentrantLock());

        Transaction transaction = new Transaction();

        try {
            fromLock.lock();
            toLock.lock();

            Client fromAccount = getByAccountNumber(fromAccountNumber);
            Client toAccount = getByAccountNumber(toAccountNumber);

            transaction.setFromAccountNumber(fromAccountNumber);
            transaction.setToAccountNumber(toAccountNumber);
            transaction.setValue(value);
            transaction.setDate(LocalDateTime.now());
            transaction.setStatus("FAILED");

            if (fromAccount.getBalance() < value) {
                throw new InsufficientBalance("Insufficient balance");
            }
            if(value > 100){
                throw new TransferAmount("Max amount to transfer is 100");
            }
            if (value <= 0) {
                throw new TransferAmount("Minimum amount to transfer is 1");
            }
            fromAccount.setBalance(fromAccount.getBalance() - value);
            toAccount.setBalance(toAccount.getBalance() + value);
            clientRepository.save(fromAccount);
            clientRepository.save(toAccount);

            transaction.setStatus("SUCCESS");

        } finally {
            fromLock.unlock();
            toLock.unlock();
            if(transaction.getFromAccountNumber() != null) {
                saveTransaction(transaction);
            }
        }
    }

    public List<Transaction> getTransactionByAccountNumber(Long accountNumber) {
        if (clientRepository.getClientByAccountNumber(accountNumber) == null) {
            throw new EntityNotFound("Client of account number " + accountNumber + " not found");
        }
        return transactionRepository.findAllByFromAccountNumberOrderByDateDesc(accountNumber);
    }

    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }*/

    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}

    /*
    public Client update(Client obj) {
        Optional<Client> optional = clientRepository.findById(obj.getId());
        if (optional.isEmpty()) {
            throw new RuntimeException("Client not found");
        }
        updateClient(optional, obj);
        return clientRepository.save(optional.get());

    }

    @Transactional
    public void updateClient(Optional<Client> newObj, Client obj) {
        newObj.get().setName(obj.getName());
        newObj.get().setAccountNumber(obj.getAccountNumber());
        newObj.get().setBalance(obj.getBalance());
    }*/

/*
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    public Client getById(Long id) {
        Optional<Client> obj = clientRepository.findById(id);
        if (obj.isEmpty()) {
            throw new RuntimeException("Client not found");
        }
        return obj.get();
    }*/