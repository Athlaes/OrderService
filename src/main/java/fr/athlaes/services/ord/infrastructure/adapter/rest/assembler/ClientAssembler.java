package fr.athlaes.services.ord.infrastructure.adapter.rest.assembler;

import fr.athlaes.services.ord.domain.Client;
import fr.athlaes.services.ord.infrastructure.adapter.rest.controller.CreditOrderController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ClientAssembler implements RepresentationModelAssembler<Client, EntityModel<Client>> {
    @Override
    public EntityModel<Client> toModel(Client entity) {
        var model = EntityModel.of(entity);
        model.add(linkTo(methodOn(CreditOrderController.class).getOne(entity.getId()))
                .withRel("Get-order"));
        return model;
    }

    @Override
    public CollectionModel<EntityModel<Client>> toCollectionModel(Iterable<? extends Client> entities) {
        var client = StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(client,
                linkTo(methodOn(CreditOrderController.class)
                        .getAll()).withSelfRel());
    }
}
