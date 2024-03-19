package fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping;

import java.util.Objects;
import java.util.UUID;

import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.CreditOrderStatus;
import fr.athlaes.services.ord.domain.TmpDecisionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CreditOrder")
public class CreditOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private Double askedAmount;

    @NotNull
    private int monthDuration;
    private Double salaryOverLast3Years;
    private TmpDecisionStatus decisionStatus;
    private Double rate;
    private Double totalDue;
    private Double totalPerMonth;
    private boolean financeValidation;

    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private AdvisorEntity advisor = new AdvisorEntity();
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    @NotNull
    private ClientEntity client;

    @NotNull
    private CreditOrderStatus status;

    public CreditOrderEntity(CreditOrder creditOrder) {
        this.id = creditOrder.getId();
        this.askedAmount = creditOrder.getAskedAmount();
        this.monthDuration = creditOrder.getMonthDuration();
        this.decisionStatus = creditOrder.getDecisionStatus();
        this.status = creditOrder.getStatus();
        this.salaryOverLast3Years = creditOrder.getSalaryOverLast3Years();
        this.rate = creditOrder.getRate();
        this.totalDue = creditOrder.getTotalDue();
        this.totalPerMonth = creditOrder.getTotalPerMonth();
        this.financeValidation = creditOrder.isFinanceValidation();
        this.client = new ClientEntity(creditOrder.getClient());
        this.advisor = Objects.nonNull(creditOrder.getAdvisor()) ? new AdvisorEntity(creditOrder.getAdvisor()) : null;
    }

    public CreditOrder toDomain() {
        return new CreditOrder(id, client.toDomain(), salaryOverLast3Years, askedAmount, monthDuration, status, decisionStatus,
                Objects.nonNull(advisor) ? advisor.toDomain() : null,
                rate, totalDue, totalPerMonth, financeValidation);
    }
}
