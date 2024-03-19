package fr.athlaes.services.ord.application.port.outgoing;

import java.util.UUID;

public interface FinanceClient {
    boolean verifyDeclaration(UUID id, Double amountOverLast3Years);
}
