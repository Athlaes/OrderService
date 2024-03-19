package fr.athlaes.services.ord.application.port.incoming;

import fr.athlaes.services.ord.application.service.exceptions.ResourceNotAccessible;
import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.TmpDecisionStatus;

import java.util.UUID;

public interface CreditOrderService {
    CreditOrder createCreditOrder(CreditOrder creditOrder);

    CreditOrder updateCreditOrder(CreditOrder creditOrder) throws ResourceNotAccessible;

    CreditOrder getCreditOrderWithFinanceValidation(UUID id);

    CreditOrder updateCreditOrderDecision(UUID id, TmpDecisionStatus status) throws ResourceNotAccessible;

    CreditOrder validateCreditOrder(UUID id, TmpDecisionStatus status) throws ResourceNotAccessible;


}
