package fr.athlaes.services.ord.application.service.exceptions;

public class ResourceNotCompleteException extends IllegalArgumentException {

    public ResourceNotCompleteException(String message) {
        super(message);
    }
}
