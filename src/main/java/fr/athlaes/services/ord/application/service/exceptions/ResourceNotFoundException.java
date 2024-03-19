package fr.athlaes.services.ord.application.service.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private UUID id;

    public ResourceNotFoundException(UUID id) {
        super();
        this.id = id;
    }

    public ResourceNotFoundException(UUID id, Throwable e) {
        super(e);
        this.id = id;
    }
}
