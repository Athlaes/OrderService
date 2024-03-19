package fr.athlaes.services.ord.application.service.exceptions;

public class ResourceNotAccessible extends IllegalAccessException {
    public ResourceNotAccessible(String message) {
        super(message);
    }
}