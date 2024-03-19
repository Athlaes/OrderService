package fr.athlaes.services.ord.infrastructure.adapter.rest.controller;

import fr.athlaes.services.ord.application.port.outgoing.ClientPersistence;
import fr.athlaes.services.ord.domain.Client;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping(value = "/api/clients", produces = MediaTypes.HAL_JSON_VALUE)
@ExposesResourceFor(Client.class)
public class ClientController {
    private final ClientPersistence clientRepository;

    public ClientController(ClientPersistence clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAll() {
        return ResponseEntity.ok(clientRepository.findAll());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientRepository.save(client));
    }

    @PatchMapping
    @Transactional
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientRepository.save(client));
    }
}
