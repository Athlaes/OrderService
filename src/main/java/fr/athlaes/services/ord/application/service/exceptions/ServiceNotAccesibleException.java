package fr.athlaes.services.ord.application.service.exceptions;

public class ServiceNotAccesibleException extends RuntimeException {
    public ServiceNotAccesibleException(String message) {
        super(message);
    }
}
