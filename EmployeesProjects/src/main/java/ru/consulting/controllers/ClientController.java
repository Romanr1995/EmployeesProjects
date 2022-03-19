package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.consulting.entitity.Client;
import ru.consulting.service.ClientService;

@RestController
@RequestMapping("client")
public class ClientController {
    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public void save(@RequestBody Client client) {
        clientService.saveNew(client);
    }

    @RequestMapping
    public @ResponseBody
    Client showByTitle(@RequestParam String title) {
        return clientService.getByTitle(title);
    }

    @DeleteMapping("{phone}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String phone) {
        clientService.deleteByPhone(phone);
    }
}
