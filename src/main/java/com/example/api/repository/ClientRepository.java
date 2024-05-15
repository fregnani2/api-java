package com.example.api.repository;

import com.example.api.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Client entity
 * Extends JpaRepository to have access to CRUD operations and create custom queries
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {

    // Custom query to get a client by id
    Client getClientById(Long id);
    // Custom query to get a client by account number
    Client getClientByAccountNumber(Integer accountNumber);

}
