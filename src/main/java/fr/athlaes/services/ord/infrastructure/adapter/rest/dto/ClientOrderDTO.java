package fr.athlaes.services.ord.infrastructure.adapter.rest.dto;

import fr.athlaes.services.ord.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;

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

    public ClientOrderDTO(CreditOrder order) {

    }

    public CreditOrder toDomain() {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(this, CreditOrder.class);
    }
}
