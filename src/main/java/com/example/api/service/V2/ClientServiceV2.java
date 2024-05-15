package com.example.api.service.V2;

import com.example.api.entity.Client;
import com.example.api.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for v2 endpoint client operations
 */
@Service // Service annotation to indicate that this class is a service
public class ClientServiceV2{
    @Autowired
    private ClientRepository clientRepository;


    /**
     * Method to update a client, checks if client doesn't exist
     * @param obj Client object to update
     */
    public Client update(Client obj) {
        Optional<Client> optional = Optional.ofNullable(clientRepository.getClientById(obj.getId()));
        if (optional.isEmpty()) {
            throw new RuntimeException("Client not found");
        }
        updateClient(optional, obj); //Call to auxiliary method to update client
        return clientRepository.save(optional.get());

    }

    /**
     * Auxiliary method to update a client, updates the client with the new object
     * @param obj Optional object to update
     * @param newClient New client object
     */
    @Transactional
    public void updateClient(Optional<Client> obj, Client newClient) {
        obj.get().setName(newClient.getName());
        obj.get().setAccountNumber(newClient.getAccountNumber());
        obj.get().setBalance(newClient.getBalance());
    }

}
