package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.consulting.dto.ClientDto;
import ru.consulting.dto.ProjectDto;
import ru.consulting.entitity.Client;
import ru.consulting.service.ClientService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("client")
public class ClientController {
    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid Client client) {
        try {
            clientService.saveNew(client);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping
    public @ResponseBody
    ClientDto showByTitle(@RequestParam String title) {
        return clientService.getByTitle(title);
    }

    @DeleteMapping("/{phone}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String phone) {
        clientService.deleteByPhone(phone);
    }

    @RequestMapping("allprojects")
    public List<ProjectDto> showAllProjects(@RequestParam String title) {
        return clientService.getAllProjects(title);
    }
}
