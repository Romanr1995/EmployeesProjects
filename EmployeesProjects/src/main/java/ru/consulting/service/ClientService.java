package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.entitity.Client;
import ru.consulting.repositories.ClientRepo;

@Service
public class ClientService {

    private ClientRepo clientRepo;

    @Autowired
    public ClientService(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    public void saveNew(Client client) {
        clientRepo.save(client);
    }

    public Client getByTitle(String title) {
        return clientRepo.findByTitleIgnoreCase(title);
    }

    public void deleteByPhone(String phone) {
        clientRepo.delete(clientRepo.findByPhone(phone).orElseThrow(() ->
                new RuntimeException("Client с phone: " + phone + " не найден.")));
    }
}
