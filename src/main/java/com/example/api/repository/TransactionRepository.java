package com.example.api.repository;

import com.example.api.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Transaction entity
 */
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    /**
     * Custom query to get all transactions by account number and order by date descending
     */
    List<Transaction> findAllByFromAccountNumberOrderByDateDesc(Integer fromAccountNumber);

    /**
     * Custom query to get all transactions by account number and order by date ascending
     */
    Page<Transaction> findAllByFromAccountNumberOrderByDateAsc(Integer fromAccountNumber, Pageable pageable);

}
