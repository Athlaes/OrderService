package fr.athlaes.services.ord.infrastructure.adapter.rest.controller;

import fr.athlaes.services.ord.application.port.outgoing.ClientPersistence;
import fr.athlaes.services.ord.domain.Client;
import fr.athlaes.services.ord.infrastructure.adapter.rest.assembler.ClientAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/clients", produces = MediaTypes.HAL_JSON_VALUE)
@ExposesResourceFor(Client.class)
public class ClientController {
    private final ClientPersistence clientRepository;

    private final ClientAssembler assembler;

    public ClientController(ClientPersistence clientRepository, ClientAssembler clientAssembler) {
        this.clientRepository = clientRepository;
        this.assembler = clientAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Client>>> getAll() {
        return ResponseEntity.ok(this.assembler.toCollectionModel(clientRepository.findAll()));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EntityModel<Client>> createClient(@RequestBody Client client) {
        return ResponseEntity.ok(this.assembler.toModel(clientRepository.save(client)));
    }

    @PatchMapping
    @Transactional
    public ResponseEntity<EntityModel<Client>> updateClient(@RequestBody Client client) {
        return ResponseEntity.ok(this.assembler.toModel(clientRepository.save(client)));
    }
}
