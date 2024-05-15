package com.example.api.service;

import com.example.api.entity.Client;
import com.example.api.entity.Transaction;
import com.example.api.repository.TransactionRepository;
import com.example.api.service.exception.EntityNotFound;
import com.example.api.service.exception.TransferAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Service for Transaction entity
 */
@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientService clientService;

    /**
     * Method to make a transfer between two accounts
     * @param fromAccountNumber the account number from which the transfer will be made
     * @param toAccountNumber the account number to which the transfer will be made
     * @param value the value to be transferred
     * @throws TransferAmount if the value is greater than 100, less than or equal to 0 or the balance is insufficient
     */
    public synchronized void  makeTransfer(Integer fromAccountNumber, Integer toAccountNumber, Double value) {
        Transaction transaction = new Transaction();

        try {
            Client fromAccount = clientService.getByAccountNumber(fromAccountNumber);

            //Set transaction data to save after confirmed that fromAccount exists

            transaction.setFromAccountNumber(fromAccountNumber);
            transaction.setToAccountNumber(toAccountNumber);
            transaction.setValue(value);
            transaction.setDate(LocalDateTime.now());
            transaction.setStatus("FAILED");

            Client toAccount = clientService.getByAccountNumber(toAccountNumber);

            if(value > 100){
                throw new TransferAmount("Max amount to transfer is 100");
            }
            if (value <= 0) {
                throw new TransferAmount("Minimum amount to transfer is 1");
            }
            if (fromAccount.getBalance() < value) {
                throw new TransferAmount("Insufficient balance");
            }
            fromAccount.setBalance(fromAccount.getBalance() - value);
            toAccount.setBalance(toAccount.getBalance() + value);
            clientService.saveClient(fromAccount);
            clientService.saveClient(toAccount);

            transaction.setStatus("SUCCESS");

        } finally {
            if(transaction.getFromAccountNumber() != null) {
                saveTransaction(transaction);
            }
        }
    }

    /**
     * Retrieves a list of Transaction entities by the account number from which the transactions were made.
     * @param accountNumber the account number from which the transactions were made
     * @throws EntityNotFound if the client does not exist
     */
    public List<Transaction> getTransactionByAccountNumber(Integer accountNumber) {
        if (clientService.getByAccountNumber(accountNumber) == null) {
            throw new EntityNotFound("Client of account number " + accountNumber + " not found");
        }
        return transactionRepository.findAllByFromAccountNumberOrderByDateDesc(accountNumber);
    }

    /**
     * Method to save a transaction in the database
     * @param transaction Transaction object to save
     */
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}



