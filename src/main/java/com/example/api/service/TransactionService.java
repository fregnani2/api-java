package com.example.api.service;

import com.example.api.entity.Client;
import com.example.api.entity.Transaction;
import com.example.api.repository.TransactionRepository;
import com.example.api.service.exception.EntityNotFound;
import com.example.api.service.exception.InsufficientBalance;
import com.example.api.service.exception.TransferAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientService clientService;

    private final ConcurrentHashMap<Long, Lock> locks = new ConcurrentHashMap<>();

    public void makeTransfer(Long fromAccountNumber, Long toAccountNumber, Double value) {
        Lock fromLock = locks.computeIfAbsent(fromAccountNumber, k -> new ReentrantLock());
        Lock toLock = locks.computeIfAbsent(toAccountNumber, k -> new ReentrantLock());

        Transaction transaction = new Transaction();

        try {
            fromLock.lock();
            toLock.lock();

            Client fromAccount = clientService.getByAccountNumber(fromAccountNumber);
            Client toAccount = clientService.getByAccountNumber(toAccountNumber);

            transaction.setFromAccountNumber(fromAccountNumber);
            transaction.setToAccountNumber(toAccountNumber);
            transaction.setValue(value);
            transaction.setDate(LocalDateTime.now());
            transaction.setStatus("FAILED");

            if(value > 100){
                throw new TransferAmount("Max amount to transfer is 100");
            }
            if (value <= 0) {
                throw new TransferAmount("Minimum amount to transfer is 1");
            }
            if (fromAccount.getBalance() < value) {
                throw new InsufficientBalance("Insufficient balance");
            }
            fromAccount.setBalance(fromAccount.getBalance() - value);
            toAccount.setBalance(toAccount.getBalance() + value);
            clientService.saveClient(fromAccount);
            clientService.saveClient(toAccount);

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
        if (clientService.getByAccountNumber(accountNumber) == null) {
            throw new EntityNotFound("Client of account number " + accountNumber + " not found");
        }
        return transactionRepository.findAllByFromAccountNumberOrderByDateDesc(accountNumber);
    }

    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}



