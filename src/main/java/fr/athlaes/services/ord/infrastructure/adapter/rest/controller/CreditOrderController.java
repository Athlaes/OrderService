package fr.athlaes.services.ord.infrastructure.adapter.rest.controller;

import java.util.List;
import java.util.UUID;

import fr.athlaes.services.ord.application.port.incoming.RateService;
import fr.athlaes.services.ord.application.service.exceptions.ResourceNotAccessibleException;
import fr.athlaes.services.ord.domain.CreditOrderStatus;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.athlaes.services.ord.application.port.incoming.CreditOrderService;
import fr.athlaes.services.ord.application.port.outgoing.CreditOrderPersistence;
import fr.athlaes.services.ord.application.service.exceptions.ResourceNotFoundException;
import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.TmpDecisionStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@ResponseBody
@RequestMapping(value = "/api/creditOrders", produces = MediaTypes.HAL_JSON_VALUE)
@ExposesResourceFor(CreditOrder.class)
public class CreditOrderController {
    private final CreditOrderPersistence creditOrderRepository;
    private final CreditOrderService creditOrderService;
    private final RateService rateService;

    // TODO : make controller return DTO instead of full object
    // TODO : make controller accept DTO instead of full object to prevent user from injecting false inputs
    
    public CreditOrderController(CreditOrderPersistence creditOrderRepository, CreditOrderService creditOrderService, RateService rateService) {
        this.creditOrderRepository = creditOrderRepository;
        this.creditOrderService = creditOrderService;
        this.rateService = rateService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CreditOrder> createOrder(@RequestBody CreditOrder creditOrder) {
        return ResponseEntity.ok(this.creditOrderService.createCreditOrder(creditOrder));
    }

    @GetMapping
    public ResponseEntity<List<CreditOrder>> getAll() {
        return ResponseEntity.ok(creditOrderRepository.findAll());
    }
    
    @PatchMapping
    @Transactional
    public ResponseEntity<CreditOrder> updateOrder(@RequestBody CreditOrder creditOrder) throws ResourceNotAccessibleException {
        return ResponseEntity.ok(this.creditOrderService.updateCreditOrder(creditOrder));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getStatus(@PathVariable("id") UUID id) {
        CreditOrder res = creditOrderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return ResponseEntity.ok(res.getStatus().toString());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CreditOrder> getOne(@PathVariable("id") UUID id) {
        CreditOrder res = creditOrderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}/study")
    public ResponseEntity<CreditOrder> studyOrder(@PathVariable("id") UUID id) {
        // TODO : Use keycloak user to save who studied order
        CreditOrder res = this.creditOrderService.getCreditOrderWithFinanceValidation(id);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{id}/study/{status}")
    @Transactional
    public ResponseEntity<CreditOrder> validateOrder(@PathVariable("id") UUID id, @PathVariable("status") TmpDecisionStatus status) throws ResourceNotAccessibleException {
        // TODO : Use keycloak user to save who validated order
        CreditOrder res = this.creditOrderService.updateCreditOrderDecision(id, status);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{id}/validation/{status}")
    @Transactional
    public ResponseEntity<CreditOrder> finalValidateOrder(@PathVariable("id") UUID id, @PathVariable("status") TmpDecisionStatus status) throws ResourceNotAccessibleException {
        CreditOrder res = this.creditOrderService.validateCreditOrder(id, status);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/advisor/{id}/status/{status}")
    public ResponseEntity<List<CreditOrder>> advisorCurrentOrders(@PathVariable("id") UUID id, @PathVariable("status") CreditOrderStatus status) {
        return ResponseEntity.ok(creditOrderRepository.findByAdvisorIdAndStatus(id, status));
    }
}
