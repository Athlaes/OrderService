package fr.athlaes.services.ord.infrastructure.adapter.rest.assembler;

import fr.athlaes.services.ord.domain.Advisor;
import fr.athlaes.services.ord.infrastructure.adapter.rest.controller.AdvisorController;
import fr.athlaes.services.ord.infrastructure.adapter.rest.controller.CreditOrderController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class AdvisorAssembler implements RepresentationModelAssembler<Advisor, EntityModel<Advisor>> {
    @Override
    public EntityModel<Advisor> toModel(Advisor entity) {
        var model = EntityModel.of(entity);
        model.add(linkTo(methodOn(CreditOrderController.class).getOne(entity.getMatricule()))
                .withRel("Get-order"));
        return model;
    }

    @Override
    public CollectionModel<EntityModel<Advisor>> toCollectionModel(Iterable<? extends Advisor> entities) {
        var client = StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(client,
                linkTo(methodOn(CreditOrderController.class)
                        .getAll()).withSelfRel());    }
}
