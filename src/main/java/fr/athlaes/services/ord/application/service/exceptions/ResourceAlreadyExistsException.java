package fr.athlaes.services.ord.application.service.exceptions;

public class ResourceAlreadyExistsException extends IllegalArgumentException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
