package fr.athlaes.services.ord.infrastructure.adapter.rest.controller;

import fr.athlaes.services.ord.application.port.outgoing.AdvisorPersistence;
import fr.athlaes.services.ord.domain.Advisor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping(value = "/api/advisors", produces = MediaTypes.HAL_JSON_VALUE)
@ExposesResourceFor(Advisor.class)
public class AdvisorController {
    private final AdvisorPersistence advisorRepository;

    public AdvisorController(AdvisorPersistence advisorRepository) {
        this.advisorRepository = advisorRepository;
    }

    @GetMapping
    public ResponseEntity<List<Advisor>> getAll() {
        return ResponseEntity.ok(advisorRepository.findAll());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Advisor> createAdvisor(@RequestBody Advisor advisor) {
        return ResponseEntity.ok(advisorRepository.save(advisor));
    }

    @PatchMapping
    @Transactional
    public ResponseEntity<Advisor> updateAdvisor(@RequestBody Advisor advisor) {
        return ResponseEntity.ok(advisorRepository.save(advisor));
    }

}
