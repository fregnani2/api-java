package com.example.api.repository;

import com.example.api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByFromAccountNumberOrderByDateDesc(Long fromAccountNumber);

}
