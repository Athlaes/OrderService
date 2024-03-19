package fr.athlaes.services.ord.infrastructure.adapter.rest.assembler;

import fr.athlaes.services.ord.application.service.exceptions.ResourceNotAccessibleException;
import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.CreditOrderStatus;
import fr.athlaes.services.ord.domain.TmpDecisionStatus;
import fr.athlaes.services.ord.infrastructure.adapter.rest.controller.CreditOrderController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CreditOrderAssembler implements RepresentationModelAssembler<CreditOrder, EntityModel<CreditOrder>> {
    @Override
    public EntityModel<CreditOrder> toModel(CreditOrder order) {
        EntityModel<CreditOrder> model = null;
        try {
            model = this.toModelWithParameter(order, false);
        } catch (ResourceNotAccessibleException e) {
            model = EntityModel.of(order);
        }
        return model;
    }

    @Override
    public CollectionModel<EntityModel<CreditOrder>> toCollectionModel(Iterable<? extends CreditOrder> entities) {
        var creditOrderModel = StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(creditOrderModel,
                linkTo(methodOn(CreditOrderController.class)
                        .getAll()).withSelfRel());
    }

    public EntityModel<CreditOrder> toModelWithParameter(CreditOrder order, boolean includeLinkForAdvisors) throws ResourceNotAccessibleException {
        var model = EntityModel.of(order);
        switch (order.getStatus()) {
            case CreditOrderStatus.Début -> {
                model.add(linkTo(methodOn(CreditOrderController.class).getOne(order.getId()))
                        .withRel("Get-order"));
            }
            case CreditOrderStatus.Etude -> {
                model.add(linkTo(methodOn(CreditOrderController.class).getOne(order.getId()))
                        .withRel("Get-order"));
                if(includeLinkForAdvisors) {
                    model.add(linkTo(methodOn(CreditOrderController.class).studyOrder(order.getId()))
                            .withRel("Study"));
                    model.add(linkTo(methodOn(CreditOrderController.class).validateOrder(order.getId(), TmpDecisionStatus.Refusée))
                            .withRel("Study-reject"));
                    model.add(linkTo(methodOn(CreditOrderController.class).validateOrder(order.getId(), TmpDecisionStatus.Acceptée))
                            .withRel("Study-accept"));
                }
            }
            case CreditOrderStatus.Validation -> {
                model.add(linkTo(methodOn(CreditOrderController.class).getOne(order.getId()))
                        .withRel("Get-order"));
                if(includeLinkForAdvisors) {
                    model.add(linkTo(methodOn(CreditOrderController.class).studyOrder(order.getId()))
                            .withRel("Study"));
                    model.add(linkTo(methodOn(CreditOrderController.class).finalValidateOrder(order.getId(), TmpDecisionStatus.Refusée))
                            .withRel("Study-reject"));
                    model.add(linkTo(methodOn(CreditOrderController.class).finalValidateOrder(order.getId(), TmpDecisionStatus.Acceptée))
                            .withRel("Study-accept"));
                }
            }
        }
        return model;
    }
}
