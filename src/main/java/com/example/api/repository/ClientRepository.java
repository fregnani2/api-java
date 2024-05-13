package com.example.api.repository;

import com.example.api.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client getClientByAccountNumber(Long accountNumber);

}
