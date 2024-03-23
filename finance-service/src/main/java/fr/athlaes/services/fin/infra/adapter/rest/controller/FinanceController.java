package fr.athlaes.services.fin.infra.adapter.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class FinanceController {
    private final Logger logger = LoggerFactory.getLogger(FinanceController.class);

    @GetMapping("/client/{id}/amount/{amount}")
    public ResponseEntity<String> getAll(@PathVariable("id") UUID id, @PathVariable Double amount) {
        String res = "ko";
        var r = Math.random();
        if (r > 0.25) {
            res = "ok";
        }
        this.logger.info("checked.");
        return ResponseEntity.ok(res);
    }
}
