package fr.athlaes.services.ord.infrastructure.adapter.rest.dto;

import fr.athlaes.services.ord.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientOrderDTO extends RepresentationModel<ClientOrderDTO> {
    private UUID id;
    private Client client;
    private Double salaryOverLast3Years;
    private Double askedAmount;
    private int monthDuration;
    private CreditOrderStatus status;

    public ClientOrderDTO(CreditOrder order) {
        this.id = order.getId();
        this.client = order.getClient();
        this.salaryOverLast3Years = order.getSalaryOverLast3Years();
        this.askedAmount = order.getAskedAmount();
        this.monthDuration = order.getMonthDuration();
        this.status = order.getStatus();
    }

    public CreditOrder toDomain() {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(this, CreditOrder.class);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ClientOrderDTO && Objects.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }
}
