package fr.athlaes.services.ord.infrastructure.adapter.rest.assembler;

import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.CreditOrderStatus;
import fr.athlaes.services.ord.domain.TmpDecisionStatus;
import fr.athlaes.services.ord.infrastructure.adapter.rest.controller.CreditOrderController;
import fr.athlaes.services.ord.infrastructure.adapter.rest.dto.ClientOrderDTO;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ClientCreditOrderAssembler implements RepresentationModelAssembler<CreditOrder, ClientOrderDTO> {
    @Override
    public ClientOrderDTO toModel(CreditOrder entity) {
        ModelMapper mapper = new ModelMapper();
        ClientOrderDTO dto = mapper.map(entity, ClientOrderDTO.class);

        dto.add(linkTo(methodOn(CreditOrderController.class).getOne(entity.getId()))
                .withRel("Get-order"));
        return dto;
    }

    @Override
    public CollectionModel<ClientOrderDTO> toCollectionModel(Iterable<? extends CreditOrder> entities) {
        var creditOrderModel = StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(creditOrderModel,
                linkTo(methodOn(CreditOrderController.class)
                        .getAll()).withSelfRel());
    }
}
