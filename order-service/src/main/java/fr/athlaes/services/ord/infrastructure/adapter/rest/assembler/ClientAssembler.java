package fr.athlaes.services.ord.infrastructure.adapter.rest.assembler;

import fr.athlaes.services.ord.domain.Client;
import fr.athlaes.services.ord.infrastructure.adapter.rest.controller.ClientController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClientAssembler implements RepresentationModelAssembler<Client, EntityModel<Client>> {
    @Override
    public EntityModel<Client> toModel(Client entity) {
        return EntityModel.of(entity);
    }

    @Override
    public CollectionModel<EntityModel<Client>> toCollectionModel(Iterable<? extends Client> entities) {
        var client = StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(client,
                linkTo(methodOn(ClientController.class)
                        .getAll()).withSelfRel());
    }
}
