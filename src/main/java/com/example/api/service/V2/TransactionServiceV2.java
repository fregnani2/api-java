package com.example.api.service.V2;

import com.example.api.entity.Transaction;
import com.example.api.repository.TransactionRepository;
import com.example.api.service.ClientService;
import com.example.api.service.exception.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service for Transaction entity version 2
 */
@Service
public class TransactionServiceV2 {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientService clientService;

    /**
     * Retrieves a page of Transaction entities by the account number from which the transactions were made.
     * @param accountNumber the account number from which the transactions were made
     * @param pageable the pagination information
     * @throws EntityNotFound if the client does not exist
     */
    public Page<Transaction> getTransactionByAccountNumber(Integer accountNumber, Pageable pageable) {
        if (clientService.getByAccountNumber(accountNumber) == null) {
            throw new EntityNotFound("Client of account number " + accountNumber + " not found");
        }
        return transactionRepository.findAllByFromAccountNumberOrderByDateAsc(accountNumber, pageable);
    }
}
