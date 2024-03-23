package fr.athlaes.services.ord.infrastructure.adapter.rest.exceptions;

import fr.athlaes.services.ord.application.service.exceptions.*;
import org.hibernate.TransientPropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ExceptionResponseHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorInfo> notFoundHandler(HttpServletRequest request, ResourceNotFoundException ex) {
        String errorMessage = String.format("Resource with id : %s is not found", ex.getId());
        ErrorInfo error = new ErrorInfo(request.getRequestURI(), request.getMethod(), errorMessage, HttpStatus.PARTIAL_CONTENT.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorInfo> alreadyExistsHandler(HttpServletRequest request, ResourceAlreadyExistsException ex) {
        ErrorInfo error = new ErrorInfo(request.getRequestURI(), request.getMethod(), ex.getMessage(), HttpStatus.PARTIAL_CONTENT.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(ResourceNotAccessibleException.class)
    public ResponseEntity<ErrorInfo> alreadyExistsHandler(HttpServletRequest request, ResourceNotAccessibleException ex) {
        ErrorInfo error = new ErrorInfo(request.getRequestURI(), request.getMethod(), ex.getMessage(), HttpStatus.PARTIAL_CONTENT.value());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotCompleteException.class)
    public ResponseEntity<ErrorInfo> notCompleteHandler(HttpServletRequest request, ResourceNotCompleteException ex) {
        ErrorInfo error = new ErrorInfo(request.getRequestURI(), request.getMethod(), ex.getMessage(), HttpStatus.PARTIAL_CONTENT.value());
        return new ResponseEntity<>(error, HttpStatus.PARTIAL_CONTENT);
    }

    @ExceptionHandler(TransientPropertyValueException.class)
    public ResponseEntity<ErrorInfo> embeddedResourceIdMissingHandler(HttpServletRequest request, TransientPropertyValueException ex) {
        ErrorInfo error = new ErrorInfo(request.getRequestURI(), request.getMethod(),"Embedded resource was missing id property", HttpStatus.PARTIAL_CONTENT.value());
        return new ResponseEntity<>(error, HttpStatus.PARTIAL_CONTENT);
    }

    @ExceptionHandler(ServiceNotAccesibleException.class)
    public ResponseEntity<ErrorInfo> embeddedResourceIdMissingHandler(HttpServletRequest request, ServiceNotAccesibleException ex) {
        ErrorInfo error = new ErrorInfo(request.getRequestURI(), request.getMethod(), ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value());
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
