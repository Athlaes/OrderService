package fr.athlaes.services.ord.infrastructure.adapter.rest.assembler;

import fr.athlaes.services.ord.application.port.incoming.CreditOrderService;
import fr.athlaes.services.ord.application.service.exceptions.ResourceNotAccessibleException;
import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.CreditOrderStatus;
import fr.athlaes.services.ord.infrastructure.adapter.rest.controller.CreditOrderController;
import fr.athlaes.services.ord.infrastructure.adapter.rest.dto.ClientOrderDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClientCreditOrderAssembler implements RepresentationModelAssembler<CreditOrder, ClientOrderDTO> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CreditOrderService creditOrderService;

    public ClientCreditOrderAssembler(CreditOrderService creditOrderService) {
        this.creditOrderService = creditOrderService;
    }

    @Override
    public ClientOrderDTO toModel(CreditOrder entity) {
        ModelMapper mapper = new ModelMapper();
        ClientOrderDTO dto = mapper.map(entity, ClientOrderDTO.class);
        try {
            if (entity.getStatus().equals(CreditOrderStatus.Debut)) {
                dto.add(linkTo(methodOn(CreditOrderController.class).updateOrder(new ClientOrderDTO(entity)))
                        .withRel("update"));
                if(this.creditOrderService.checkOrderCompletion(entity)) {
                    dto.add(linkTo(methodOn(CreditOrderController.class).submitOrder(entity.getId()))
                            .withRel("submit"));
                }
            }
        } catch (ResourceNotAccessibleException e) {
            logger.warn("La resource avec l'id {} n'a pas pu être mappé vers son modèle correctement", entity.getId());
        }
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
                        .getAll(creditOrderModel.getFirst().getClient().getId())).withSelfRel());
    }
}
