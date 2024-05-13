package com.example.api.service;

import com.example.api.entity.Client;
import com.example.api.entity.Transaction;
import com.example.api.repository.TransactionRepository;
import com.example.api.service.exception.InsufficientBalance;
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
        Client fromAccount = new Client(1L,"Bruno", 324L,1000.0);
        Client toAccount = new Client(2L,"Marcos", 120L,5000.0);

        when(clientService.getByAccountNumber(324L)).thenReturn(fromAccount);
        when(clientService.getByAccountNumber(120L)).thenReturn(toAccount);

        transactionService.makeTransfer(324L, 120L, 100.0);


        verify(clientService, times(1)).getByAccountNumber(324L);
        verify(clientService, times(1)).getByAccountNumber(120L);
        verify(clientService, times(1)).saveClient(fromAccount);
        verify(clientService, times(1)).saveClient(toAccount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));

    }


    @Test
    @DisplayName("Should not transfer an amount greater than 100 or less than 1 or greater than balance")
    void makeTransferWrongAmount() {
        Client fromAccount = new Client(1L,"Bruno", 324L,50.0);
        Client toAccount = new Client(2L,"Marcos", 120L,5000.0);

        when(clientService.getByAccountNumber(324L)).thenReturn(fromAccount);
        when(clientService.getByAccountNumber(120L)).thenReturn(toAccount);

        assertThrows(TransferAmount.class, () -> transactionService.makeTransfer(324L, 120L, 200.0));
        assertThrows(TransferAmount.class, () -> transactionService.makeTransfer(324L, 120L, 0.0));
        assertThrows(InsufficientBalance.class, () -> transactionService.makeTransfer(324L, 120L, 70.0));
    }

    @Test
    @DisplayName("Should get all transactions by account number")
    void getTransactionByAccountNumber() {
        // Arrange
        Client fromAccount = new Client(1L,"Bruno", 324L,1000.0);
        Client toAccount = new Client(2L,"Marcos", 120L,5000.0);

        when(clientService.getByAccountNumber(324L)).thenReturn(fromAccount);
        when(clientService.getByAccountNumber(120L)).thenReturn(toAccount);
        


        Transaction transaction1 = new Transaction(1L, 324L, 120L, 100.0, LocalDateTime.now(), "SUCCESS");
        Transaction transaction2 = new Transaction(2L, 324L, 120L, 100.0, LocalDateTime.now(), "SUCCESS");
        Transaction transaction3 = new Transaction(3L, 324L, 120L, 100.0, LocalDateTime.now(), "SUCCESS");
        Transaction transaction4 = new Transaction(4L, 324L, 120L, 0.0, LocalDateTime.now(), "FAILED");


        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3, transaction4);

        when(transactionRepository.findAllByFromAccountNumberOrderByDateDesc(324L)).thenReturn(transactions);

        // Act
        List<Transaction> returnedTransactions = transactionService.getTransactionByAccountNumber(324L);

        // Assert
        assertEquals(4, returnedTransactions.size());
    }

    @Test
    @DisplayName("Should record a failed transaction")
    void makeTransferFailed() {
        Client fromAccount = new Client(1L,"Bruno", 324L,50.0);
        Client toAccount = new Client(2L,"Marcos", 120L,5000.0);

        when(clientService.getByAccountNumber(324L)).thenReturn(fromAccount);
        when(clientService.getByAccountNumber(120L)).thenReturn(toAccount);

        assertThrows(InsufficientBalance.class, () -> transactionService.makeTransfer(324L, 120L, 70.0));

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(1)).save(transactionCaptor.capture());

        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals("FAILED", savedTransaction.getStatus());
    }

}