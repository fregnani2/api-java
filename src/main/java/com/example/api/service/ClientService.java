package com.example.api.service;

import com.example.api.entity.Client;
import com.example.api.repository.ClientRepository;
import com.example.api.repository.TransactionRepository;
import com.example.api.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service for client operations in Version 1
 */
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Method to create a client
     * @param obj
     * @throws DuplicateAccount if the account number already exists
     * @throws WrongArgument if the name is null or empty or the balance is negative or null
     */

    public Client create(Client obj) {
        if (clientRepository.getClientByAccountNumber(obj.getAccountNumber()) != null) {
            throw new DuplicateAccount("Account number already exists");
        }
        if(obj.getName() == null || obj.getName().isEmpty()){
            throw new WrongArgument("Name cannot be null or empty");
        }
        if (obj.getBalance()== null || obj.getBalance() < 0) {
            throw new WrongArgument("Balance cannot be negative or null");
        }
        return clientRepository.save(obj);
    }

    /**
     * Method to return a client by account number
     * @param accountNumber the account number of the client
     * @throws EntityNotFound if the client does not exist
     */
    public Client getByAccountNumber(Integer accountNumber) {
        Optional<Client> obj = Optional.ofNullable(clientRepository.getClientByAccountNumber(accountNumber));
        if (obj.isEmpty()) {
            throw new EntityNotFound("Client of account number " + accountNumber + " not found");
        }
        return clientRepository.getClientByAccountNumber(accountNumber);

    }

    /**
     * Method to return all clients
     */
    public List<Client> getAll() {
        return clientRepository.findAll();
    }


    /**
     * Method to save a client in the database
     * @param client Client object to save
     */
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
