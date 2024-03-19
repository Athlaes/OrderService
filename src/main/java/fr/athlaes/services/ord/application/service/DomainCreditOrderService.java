package fr.athlaes.services.ord.application.service;

import fr.athlaes.services.ord.application.ddd.DomainService;
import fr.athlaes.services.ord.application.port.incoming.AdvisorService;
import fr.athlaes.services.ord.application.port.incoming.CreditOrderService;
import fr.athlaes.services.ord.application.port.outgoing.CreditOrderPersistence;
import fr.athlaes.services.ord.application.port.outgoing.FinanceClient;
import fr.athlaes.services.ord.application.port.outgoing.StatusHistoryPersistence;
import fr.athlaes.services.ord.application.service.exceptions.ResourceAlreadyExistsException;
import fr.athlaes.services.ord.application.service.exceptions.ResourceNotAccessibleException;
import fr.athlaes.services.ord.application.service.exceptions.ResourceNotFoundException;
import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.CreditOrderStatus;
import fr.athlaes.services.ord.domain.StatusHistory;
import fr.athlaes.services.ord.domain.TmpDecisionStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@DomainService
public class DomainCreditOrderService implements CreditOrderService {
    CreditOrderPersistence repository;
    StatusHistoryPersistence statusHistoryRepository;
    AdvisorService advisorService;
    FinanceClient financeClient;

    public DomainCreditOrderService(CreditOrderPersistence creditOrderPersistence, StatusHistoryPersistence statusHistoryRepository, AdvisorService advisorService, FinanceClient financeClient) {
        this.repository = creditOrderPersistence;
        this.statusHistoryRepository = statusHistoryRepository;
        this.advisorService = advisorService;
        this.financeClient = financeClient;
    }

    @Override
    public CreditOrder createCreditOrder(CreditOrder creditOrder) {
        if (Objects.nonNull(creditOrder.getId())) {
            throw new ResourceAlreadyExistsException(String.format("Object already contains id %s, to update existing object use patch", creditOrder.getId()));
        }
        creditOrder.setDecisionStatus(null);
        creditOrder.setStatus(CreditOrderStatus.Début);
        creditOrder.setFinanceValidation(false);

        CreditOrder fCreditOrder;
        if(this.checkOrderCompletion(creditOrder)) {
            fCreditOrder = this.completeOrder(creditOrder);
        } else {
            fCreditOrder = this.repository.save(creditOrder);
        }
        this.saveStatus(fCreditOrder);
        return creditOrder;
    }

    @Override
    public CreditOrder updateCreditOrder(CreditOrder creditOrder) throws ResourceNotAccessibleException {
        if(!creditOrder.getStatus().equals(CreditOrderStatus.Début)) {
            throw new ResourceNotAccessibleException("Resource cannot be updated anymore because it is being validated");
        }

        CreditOrder foundCreditOrder = this.repository.findById(creditOrder.getId())
                .orElseThrow(() -> new ResourceNotFoundException(creditOrder.getId()));
        foundCreditOrder.setAskedAmount(creditOrder.getAskedAmount());
        foundCreditOrder.setSalaryOverLast3Years(creditOrder.getSalaryOverLast3Years());
        foundCreditOrder.setMonthDuration(creditOrder.getMonthDuration());

        if(this.checkOrderCompletion(foundCreditOrder)) {
            foundCreditOrder = this.completeOrder(foundCreditOrder);
        } else {
            foundCreditOrder = this.repository.save(foundCreditOrder);
        }
        this.saveStatus(foundCreditOrder);
        return foundCreditOrder;
    }

    @Override
    public CreditOrder getCreditOrderWithFinanceValidation(UUID id) {
        CreditOrder creditOrder = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        creditOrder.setFinanceValidation(this.financeClient.verifyDeclaration(id, creditOrder.getSalaryOverLast3Years()));
        this.repository.save(creditOrder);
        return creditOrder;
    }

    @Override
    public CreditOrder updateCreditOrderDecision(UUID id, TmpDecisionStatus status) throws ResourceNotAccessibleException {
        CreditOrder creditOrder = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if (!creditOrder.getStatus().equals(CreditOrderStatus.Etude)){
            throw new ResourceNotAccessibleException("Resource decision can't be updated now. It is either not complete or being validated.");
        }
        creditOrder.setDecisionStatus(status);
        creditOrder.setStatus(CreditOrderStatus.Validation);
        this.saveStatus(creditOrder);
        return creditOrder;
    }

    @Override
    public CreditOrder validateCreditOrder(UUID id, TmpDecisionStatus status) throws ResourceNotAccessibleException {
        CreditOrder creditOrder = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if (!creditOrder.getStatus().equals(CreditOrderStatus.Validation)){
            throw new ResourceNotAccessibleException("Resource decision can't be validated now.");
        }
        creditOrder.setDecisionStatus(status);
        if (status.equals(TmpDecisionStatus.Acceptée)) {
            creditOrder.setStatus(CreditOrderStatus.Acceptation);
        } else {
            creditOrder.setStatus(CreditOrderStatus.Rejet);
        }
        this.repository.save(creditOrder);
        this.saveStatus(creditOrder);
        return creditOrder;
    }

    private CreditOrder completeOrder(final CreditOrder order) {
        order.setStatus(CreditOrderStatus.Etude);
        order.setAdvisor(this.advisorService.getRandomAdvisor());
        return this.repository.save(order);
    }

    private void saveStatus(CreditOrder creditOrder) {
        this.statusHistoryRepository.save(new StatusHistory(null, creditOrder.getId(), creditOrder.getStatus(), LocalDate.now(), creditOrder.getDecisionStatus(), creditOrder.getAdvisor().getMatricule()));
    }

    private boolean checkOrderCompletion(CreditOrder order) {
        return Objects.nonNull(order.getAskedAmount())
                && Objects.nonNull(order.getSalaryOverLast3Years())
                && Objects.nonNull(order.getMonthDuration());
    }
}
