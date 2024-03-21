package fr.athlaes.services.ord.application.port.incoming;

import fr.athlaes.services.ord.application.service.exceptions.ResourceNotAccessibleException;
import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.TmpDecisionStatus;

import java.util.UUID;

public interface CreditOrderService {
    CreditOrder createCreditOrder(CreditOrder creditOrder);

    CreditOrder updateCreditOrder(CreditOrder creditOrder) throws ResourceNotAccessibleException;

    CreditOrder getCreditOrderWithFinanceValidation(UUID id);

    CreditOrder updateCreditOrderDecision(UUID id, TmpDecisionStatus status) throws ResourceNotAccessibleException;

    CreditOrder validateCreditOrder(UUID id, TmpDecisionStatus status) throws ResourceNotAccessibleException;

    CreditOrder submitCreditOrder(UUID id) throws ResourceNotAccessibleException;

    boolean checkOrderCompletion(CreditOrder creditOrder);
}
