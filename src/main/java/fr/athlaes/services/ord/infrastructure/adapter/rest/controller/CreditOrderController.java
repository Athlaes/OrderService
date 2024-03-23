package fr.athlaes.services.ord.infrastructure.adapter.rest.controller;

import java.util.UUID;

import fr.athlaes.services.ord.application.service.exceptions.ResourceNotAccessibleException;
import fr.athlaes.services.ord.application.service.exceptions.ServiceNotAccesibleException;
import fr.athlaes.services.ord.domain.CreditOrderStatus;
import fr.athlaes.services.ord.infrastructure.adapter.rest.assembler.ClientCreditOrderAssembler;
import fr.athlaes.services.ord.infrastructure.adapter.rest.assembler.CreditOrderAssembler;
import fr.athlaes.services.ord.infrastructure.adapter.rest.dto.ClientOrderDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import fr.athlaes.services.ord.application.port.incoming.CreditOrderService;
import fr.athlaes.services.ord.application.port.outgoing.CreditOrderPersistence;
import fr.athlaes.services.ord.application.service.exceptions.ResourceNotFoundException;
import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.TmpDecisionStatus;

@RestController
@RequestMapping(value = "/api/creditOrders", produces = MediaTypes.HAL_JSON_VALUE)
@ExposesResourceFor(CreditOrder.class)
public class CreditOrderController {
    private final CreditOrderPersistence creditOrderRepository;
    private final CreditOrderService creditOrderService;
    private final CreditOrderAssembler creditOrderAssembler;
    private final ClientCreditOrderAssembler clientCreditOrderAssembler;
    private final ModelMapper modelMapper = new ModelMapper();

    public CreditOrderController(CreditOrderPersistence creditOrderRepository, CreditOrderService creditOrderService, CreditOrderAssembler creditOrderAssembler, ClientCreditOrderAssembler clientCreditOrderAssembler) {
        this.creditOrderRepository = creditOrderRepository;
        this.creditOrderService = creditOrderService;
        this.creditOrderAssembler = creditOrderAssembler;
        this.clientCreditOrderAssembler = clientCreditOrderAssembler;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ClientOrderDTO> createOrder(@RequestBody ClientOrderDTO creditOrder) {
        return ResponseEntity.ok(this.clientCreditOrderAssembler.toModel(this.creditOrderService.createCreditOrder(creditOrder.toDomain())));
    }

    @PatchMapping("/{id}/submit")
    @Transactional
    public ResponseEntity<ClientOrderDTO> submitOrder(@PathVariable("id") UUID id) throws ResourceNotAccessibleException {
        return ResponseEntity.ok(this.modelMapper.map(this.creditOrderService.submitCreditOrder(id), ClientOrderDTO.class));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<CollectionModel<ClientOrderDTO>> getAll(@PathVariable("id") UUID id) {
        // TODO : should be protected as anyone can get all credit order from anyone and update it
        return ResponseEntity.ok(this.clientCreditOrderAssembler.toCollectionModel(creditOrderRepository.findAllByClientId(id)));
    }

    // TODO : Authent with keycloak
    @PatchMapping
    @Transactional
    public ResponseEntity<ClientOrderDTO> updateOrder(@RequestBody ClientOrderDTO creditOrder) throws ResourceNotAccessibleException {
        return ResponseEntity.ok(this.clientCreditOrderAssembler.toModel(this.creditOrderService.updateCreditOrder(creditOrder.toDomain())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientOrderDTO> getOne(@PathVariable("id") UUID id) {
        CreditOrder res = creditOrderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return ResponseEntity.ok(this.clientCreditOrderAssembler.toModel(res));
    }

    // TODO : Authent with keycloak
    @GetMapping("/{id}/study")
    @CircuitBreaker(name = "orderservice")
    @Retry(name = "orderservice", fallbackMethod = "fallbackFinance")
    public ResponseEntity<EntityModel<CreditOrder>> studyOrder(@PathVariable("id") UUID id) {
        // TODO : Use keycloak user to save who studied order
        CreditOrder res = this.creditOrderService.getCreditOrderWithFinanceValidation(id);
        return ResponseEntity.ok(this.creditOrderAssembler.toModel(res));
    }

    private ResponseEntity<EntityModel<CreditOrder>> fallbackFinance(RuntimeException re) {
        // TODO : try moving it into the client rather than in the controller
        throw new ServiceNotAccesibleException("Impossible de contacter le service finance");
    }

    // TODO : Authent with keycloak
    @PatchMapping("/{id}/study/{status}")
    @Transactional
    public ResponseEntity<EntityModel<CreditOrder>> validateOrder(@PathVariable("id") UUID id, @PathVariable("status") TmpDecisionStatus status) throws ResourceNotAccessibleException {
        // TODO : Use keycloak user to save who validated order
        CreditOrder res = this.creditOrderService.updateCreditOrderDecision(id, status);
        return ResponseEntity.ok(this.creditOrderAssembler.toModel(res));
    }

    // TODO : Authent with keycloak
    @PatchMapping("/{id}/validation/{status}")
    @Transactional
    public ResponseEntity<EntityModel<CreditOrder>> finalValidateOrder(@PathVariable("id") UUID id, @PathVariable("status") TmpDecisionStatus status) throws ResourceNotAccessibleException {
        CreditOrder res = this.creditOrderService.validateCreditOrder(id, status);
        return ResponseEntity.ok(this.creditOrderAssembler.toModel(res));
    }

    // TODO : Authent with keycloak
    @GetMapping("/advisor/{id}/status/{status}")
    public ResponseEntity<CollectionModel<EntityModel<CreditOrder>>> advisorCurrentOrders(@PathVariable("id") UUID id, @PathVariable("status") CreditOrderStatus status) {
        return ResponseEntity.ok(this.creditOrderAssembler.toCollectionModel(creditOrderRepository.findByAdvisorIdAndStatus(id, status)));
    }
}
