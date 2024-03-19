package fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping;

import java.time.LocalDate;
import java.util.UUID;

import fr.athlaes.services.ord.domain.CreditOrderStatus;
import fr.athlaes.services.ord.domain.StatusHistory;
import fr.athlaes.services.ord.domain.TmpDecisionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "StatusHistory")
public class StatusHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private CreditOrderStatus update;

    @NotNull
    private LocalDate updateDate;

    @NotNull
    private UUID creditOrderId;

    private TmpDecisionStatus tmpDecisionStatus;

    private UUID advisorId;

    public StatusHistoryEntity(StatusHistory statusUpdates) {
        this.id = statusUpdates.getId();
        this.update = statusUpdates.getUpdate();
        this.updateDate = statusUpdates.getUpdateDate();
        this.tmpDecisionStatus = statusUpdates.getTmpDecisionStatus();
        this.advisorId = statusUpdates.getAdvisorId();
    }

    public StatusHistory toDomain() {
        return new StatusHistory(id, creditOrderId, update, updateDate, tmpDecisionStatus, advisorId);
    }
}
