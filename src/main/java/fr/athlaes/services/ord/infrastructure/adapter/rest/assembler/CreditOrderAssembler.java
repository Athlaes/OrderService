package fr.athlaes.services.ord.infrastructure.adapter.rest.assembler;

import fr.athlaes.services.ord.application.service.exceptions.ResourceNotAccessibleException;
import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.CreditOrderStatus;
import fr.athlaes.services.ord.domain.TmpDecisionStatus;
import fr.athlaes.services.ord.infrastructure.adapter.rest.controller.CreditOrderController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CreditOrderAssembler implements RepresentationModelAssembler<CreditOrder, EntityModel<CreditOrder>> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public EntityModel<CreditOrder> toModel(CreditOrder order) {
        var model = EntityModel.of(order);
        try {
            switch (order.getStatus()) {
                case CreditOrderStatus.Etude -> {
                    model.add(linkTo(methodOn(CreditOrderController.class).validateOrder(order.getId(), TmpDecisionStatus.Refusee))
                            .withRel("reject"));
                    model.add(linkTo(methodOn(CreditOrderController.class).validateOrder(order.getId(), TmpDecisionStatus.Acceptee))
                            .withRel("accept"));
                }
                case CreditOrderStatus.Validation -> {
                    model.add(linkTo(methodOn(CreditOrderController.class).finalValidateOrder(order.getId(), TmpDecisionStatus.Refusee))
                            .withRel("reject"));
                    model.add(linkTo(methodOn(CreditOrderController.class).finalValidateOrder(order.getId(), TmpDecisionStatus.Acceptee))
                            .withRel("accept"));
                }
                default -> {
                    break;
                }
            }
        } catch (ResourceNotAccessibleException e) {
            logger.warn("La resource avec l'id {} n'a pas pu être mappé vers son modèle correctement", order.getId());
        }
        return model;
    }

    @Override
    public CollectionModel<EntityModel<CreditOrder>> toCollectionModel(Iterable<? extends CreditOrder> entities) {
        var creditOrderModel = StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();
        var content = creditOrderModel.getFirst().getContent();
        if (Objects.nonNull(content)) {
            return CollectionModel.of(creditOrderModel,
                    linkTo(methodOn(CreditOrderController.class)
                            .advisorCurrentOrders(content.getAdvisor().getMatricule(), content.getStatus())).withSelfRel());
        } else {
            return CollectionModel.of(creditOrderModel);
        }
    }
}