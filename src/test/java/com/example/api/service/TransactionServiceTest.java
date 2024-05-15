package com.example.api.service;

import com.example.api.entity.Client;
import com.example.api.entity.Transaction;
import com.example.api.repository.TransactionRepository;
import com.example.api.service.exception.TransferAmount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TransactionService
 */
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ClientService clientService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should make a transfer between two accounts")
    void makeTransfer() throws Exception {
        Client fromAccount = new Client(1L,"Bruno", 324,1000.0);
        Client toAccount = new Client(2L,"Marcos", 120,5000.0);

        when(clientService.getByAccountNumber(324)).thenReturn(fromAccount);
        when(clientService.getByAccountNumber(120)).thenReturn(toAccount);

        transactionService.makeTransfer(324, 120, 100.0);


        verify(clientService, times(1)).getByAccountNumber(324);
        verify(clientService, times(1)).getByAccountNumber(120);
        verify(clientService, times(1)).saveClient(fromAccount);
        verify(clientService, times(1)).saveClient(toAccount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));

    }


    @Test
    @DisplayName("Should not transfer an amount greater than 100 or less than 1 or greater than balance")
    void makeTransferWrongAmount() {
        Client fromAccount = new Client(1L,"Bruno", 324,50.0);
        Client toAccount = new Client(2L,"Marcos", 120,5000.0);

        when(clientService.getByAccountNumber(324)).thenReturn(fromAccount);
        when(clientService.getByAccountNumber(120)).thenReturn(toAccount);

        assertThrows(TransferAmount.class, () -> transactionService.makeTransfer(324, 120, 200.0));
        assertThrows(TransferAmount.class, () -> transactionService.makeTransfer(324, 120, 0.0));
        assertThrows(TransferAmount.class, () -> transactionService.makeTransfer(324, 120, 70.0));
    }

    @Test
    @DisplayName("Should get all transactions by account number")
    void getTransactionByAccountNumber() {
        // Arrange
        Client fromAccount = new Client(1L,"Bruno", 324,1000.0);
        Client toAccount = new Client(2L,"Marcos", 120,5000.0);

        when(clientService.getByAccountNumber(324)).thenReturn(fromAccount);
        when(clientService.getByAccountNumber(120)).thenReturn(toAccount);
        


        Transaction transaction1 = new Transaction(1L, 324, 120, 100.0, LocalDateTime.now(), "SUCCESS");
        Transaction transaction2 = new Transaction(2L, 324, 120, 100.0, LocalDateTime.now(), "SUCCESS");
        Transaction transaction3 = new Transaction(3L, 324, 120, 100.0, LocalDateTime.now(), "SUCCESS");
        Transaction transaction4 = new Transaction(4L, 324, 120, 0.0, LocalDateTime.now(), "FAILED");


        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3, transaction4);

        when(transactionRepository.findAllByFromAccountNumberOrderByDateDesc(324)).thenReturn(transactions);

        // Act
        List<Transaction> returnedTransactions = transactionService.getTransactionByAccountNumber(324);

        // Assert
        assertEquals(4, returnedTransactions.size());
    }

    @Test
    @DisplayName("Should save a failed transaction")
    void makeTransferFailed() {
        Client fromAccount = new Client(1L,"Bruno", 324,50.0);
        Client toAccount = new Client(2L,"Marcos", 120,5000.0);

        when(clientService.getByAccountNumber(324)).thenReturn(fromAccount);
        when(clientService.getByAccountNumber(120)).thenReturn(toAccount);

        assertThrows(TransferAmount.class, () -> transactionService.makeTransfer(324, 120, 70.0));

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(1)).save(transactionCaptor.capture());

        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals("FAILED", savedTransaction.getStatus());
    }



}