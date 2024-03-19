package fr.athlaes.services.ord.application.service.exceptions;

public class ResourceNotAccessibleException extends IllegalAccessException {
    public ResourceNotAccessibleException(String message) {
        super(message);
    }
}