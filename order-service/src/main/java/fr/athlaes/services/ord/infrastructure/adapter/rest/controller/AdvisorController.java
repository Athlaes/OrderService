package fr.athlaes.services.ord.infrastructure.adapter.rest.controller;

import fr.athlaes.services.ord.application.port.outgoing.AdvisorPersistence;
import fr.athlaes.services.ord.domain.Advisor;
import fr.athlaes.services.ord.infrastructure.adapter.rest.assembler.AdvisorAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/advisors", produces = MediaTypes.HAL_JSON_VALUE)
@ExposesResourceFor(Advisor.class)
public class AdvisorController {
    private final AdvisorPersistence advisorRepository;

    private final AdvisorAssembler assembler;

    public AdvisorController(AdvisorPersistence advisorRepository, AdvisorAssembler assembler) {
        this.advisorRepository = advisorRepository;
        this.assembler = assembler;
    }

    // TODO : Authent with keycloak
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Advisor>>> getAll() {
        return ResponseEntity.ok(this.assembler.toCollectionModel(advisorRepository.findAll()));
    }

    // TODO : Authent with keycloak
    @PostMapping
    @Transactional
    public ResponseEntity<EntityModel<Advisor>> createAdvisor(@RequestBody Advisor advisor) {
        return ResponseEntity.ok(this.assembler.toModel(advisorRepository.save(advisor)));
    }

    // TODO : Authent with keycloak
    @PatchMapping
    @Transactional
    public ResponseEntity<EntityModel<Advisor>> updateAdvisor(@RequestBody Advisor advisor) {
        return ResponseEntity.ok(this.assembler.toModel(advisorRepository.save(advisor)));
    }

}
