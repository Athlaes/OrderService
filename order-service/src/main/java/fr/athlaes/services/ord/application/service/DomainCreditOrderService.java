package fr.athlaes.services.ord.application.service;

import fr.athlaes.services.ord.application.ddd.DomainService;
import fr.athlaes.services.ord.application.port.incoming.AdvisorService;
import fr.athlaes.services.ord.application.port.incoming.CreditOrderService;
import fr.athlaes.services.ord.application.port.incoming.RateService;
import fr.athlaes.services.ord.application.port.outgoing.CreditOrderPersistence;
import fr.athlaes.services.ord.application.port.outgoing.FinanceClient;
import fr.athlaes.services.ord.application.port.outgoing.StatusHistoryPersistence;
import fr.athlaes.services.ord.application.service.exceptions.ResourceAlreadyExistsException;
import fr.athlaes.services.ord.application.service.exceptions.ResourceNotAccessibleException;
import fr.athlaes.services.ord.application.service.exceptions.ResourceNotCompleteException;
import fr.athlaes.services.ord.application.service.exceptions.ResourceNotFoundException;
import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.CreditOrderStatus;
import fr.athlaes.services.ord.domain.StatusHistory;
import fr.athlaes.services.ord.domain.TmpDecisionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@DomainService
public class DomainCreditOrderService implements CreditOrderService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CreditOrderPersistence repository;
    private final StatusHistoryPersistence statusHistoryRepository;
    private final AdvisorService advisorService;
    private final FinanceClient financeClient;
    private final RateService rateService;

    public DomainCreditOrderService(CreditOrderPersistence creditOrderPersistence,
                                    StatusHistoryPersistence statusHistoryRepository,
                                    AdvisorService advisorService,
                                    FinanceClient financeClient,
                                    RateService rateService) {
        this.repository = creditOrderPersistence;
        this.statusHistoryRepository = statusHistoryRepository;
        this.advisorService = advisorService;
        this.financeClient = financeClient;
        this.rateService = rateService;
    }

    @Override
    public CreditOrder createCreditOrder(CreditOrder creditOrder) {
        if (Objects.nonNull(creditOrder.getId())) {
            throw new ResourceAlreadyExistsException(String.format("Resource already contains id %s, to update existing resource use patch", creditOrder.getId()));
        }
        creditOrder.setDecisionStatus(null);
        creditOrder.setStatus(CreditOrderStatus.Debut);
        creditOrder.setFinanceValidation(false);
        creditOrder.setRate(0.0);
        creditOrder.setTotalDue(0.0);
        creditOrder.setTotalPerMonth(0.0);

        CreditOrder fCreditOrder;
        fCreditOrder = this.repository.save(creditOrder);
        return fCreditOrder;
    }

    @Override
    public CreditOrder updateCreditOrder(CreditOrder creditOrder) throws ResourceNotAccessibleException {
        if (Objects.isNull(creditOrder.getId())) {
            throw new ResourceNotCompleteException("Resource doesn't contain any id, to create a new resource use post");
        }
        if(!CreditOrderStatus.Debut.equals(creditOrder.getStatus())) {
            throw new ResourceNotAccessibleException("Resource cannot be updated anymore because it is being validated");
        }
        CreditOrder foundCreditOrder = this.repository.findById(creditOrder.getId())
                .orElseThrow(() -> new ResourceNotFoundException(creditOrder.getId()));

        foundCreditOrder.setAskedAmount(creditOrder.getAskedAmount());
        foundCreditOrder.setSalaryOverLast3Years(creditOrder.getSalaryOverLast3Years());
        foundCreditOrder.setMonthDuration(creditOrder.getMonthDuration());

        return foundCreditOrder;
    }

    @Override
    public CreditOrder submitCreditOrder(UUID id) throws ResourceNotAccessibleException {
        CreditOrder foundCreditOrder = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if(!CreditOrderStatus.Debut.equals(foundCreditOrder.getStatus())) {
            throw new ResourceNotAccessibleException("Resource cannot be updated anymore because it is being validated");
        }
        if(this.checkOrderCompletion(foundCreditOrder)) {
            foundCreditOrder = this.completeOrder(foundCreditOrder);
            this.saveStatus(foundCreditOrder);
        } else {
            throw new ResourceNotCompleteException("Information is missing on the request, you can't submit it right now");
        }
        return foundCreditOrder;
    }

    @Override
    public CreditOrder getCreditOrderWithFinanceValidation(UUID id) {
        CreditOrder creditOrder = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if (CreditOrderStatus.Etude.equals(creditOrder.getStatus())) {
            creditOrder.setFinanceValidation(this.financeClient.verifyDeclaration(id, creditOrder.getSalaryOverLast3Years()));
        } else {
            this.logger.warn("Credit order {} is not in etude status, order wasn't verify with finance service", creditOrder.getId());
        }
        this.repository.save(creditOrder);
        return creditOrder;
    }

    @Override
    public CreditOrder updateCreditOrderDecision(UUID id, TmpDecisionStatus status) throws ResourceNotAccessibleException {
        CreditOrder creditOrder = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if (!CreditOrderStatus.Etude.equals(creditOrder.getStatus())) {
            throw new ResourceNotAccessibleException("Resource decision can't be updated now. It is either not complete or being validated.");
        }
        creditOrder.setDecisionStatus(status);
        creditOrder.setStatus(CreditOrderStatus.Validation);
        this.repository.save(creditOrder);
        this.saveStatus(creditOrder);
        return creditOrder;
    }

    @Override
    public CreditOrder validateCreditOrder(UUID id, TmpDecisionStatus status) throws ResourceNotAccessibleException {
        CreditOrder creditOrder = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if (!CreditOrderStatus.Validation.equals(creditOrder.getStatus())){
            throw new ResourceNotAccessibleException("Resource decision can't be validated now.");
        }
        creditOrder.setDecisionStatus(status);
        if (status.equals(TmpDecisionStatus.Acceptee)) {
            creditOrder.setStatus(CreditOrderStatus.Acceptation);
            creditOrder.setRate(this.rateService.getCurrentRate());
            creditOrder.setTotalDue(creditOrder.getAskedAmount() * creditOrder.getRate());
            creditOrder.setTotalPerMonth(creditOrder.getTotalDue() / creditOrder.getMonthDuration());
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

    public boolean checkOrderCompletion(CreditOrder order) {
        return Objects.nonNull(order.getAskedAmount())
                && Objects.nonNull(order.getSalaryOverLast3Years())
                && order.getMonthDuration() >= 2;
    }
}
