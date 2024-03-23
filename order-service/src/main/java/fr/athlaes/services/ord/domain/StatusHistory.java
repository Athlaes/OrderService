package fr.athlaes.services.ord.domain;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistory {
    private UUID id;
    private UUID creditOrderId;
    private CreditOrderStatus update;
    private LocalDate updateDate = LocalDate.now();
    private TmpDecisionStatus tmpDecisionStatus;
    private UUID advisorId;
}
