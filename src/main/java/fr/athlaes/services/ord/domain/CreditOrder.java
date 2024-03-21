package fr.athlaes.services.ord.domain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditOrder {
    private UUID id;
    private Client client;
    private Double salaryOverLast3Years;
    private Double askedAmount;
    private int monthDuration;
    private CreditOrderStatus status = CreditOrderStatus.Debut;
    private TmpDecisionStatus decisionStatus;
    private Advisor advisor;
    private Double rate;
    private Double totalDue;
    private Double totalPerMonth;
    private boolean financeValidation;
}
