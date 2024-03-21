package fr.athlaes.services.ord.infrastructure.adapter.rest.exceptions;

import fr.athlaes.services.ord.application.service.exceptions.ResourceAlreadyExistsException;
import fr.athlaes.services.ord.application.service.exceptions.ResourceNotAccessibleException;
import fr.athlaes.services.ord.application.service.exceptions.ResourceNotCompleteException;
import org.hibernate.TransientPropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fr.athlaes.services.ord.application.service.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ExceptionResponseHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> notFoundHandler(HttpServletRequest request, ResourceNotFoundException ex) {
        String errorMessage = String.format("Resource with id : %s is not found", ex.getId());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String> alreadyExistsHandler(HttpServletRequest request, ResourceAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(ResourceNotAccessibleException.class)
    public ResponseEntity<String> alreadyExistsHandler(HttpServletRequest request, ResourceNotAccessibleException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotCompleteException.class)
    public ResponseEntity<String> notCompleteHandler(HttpServletRequest request, ResourceNotCompleteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.PARTIAL_CONTENT);
    }

    @ExceptionHandler(TransientPropertyValueException.class)
    public ResponseEntity<String> embeddedResourceIdMissingHandler(HttpServletRequest request, TransientPropertyValueException ex) {
        return new ResponseEntity<>("Embedded resource was missing id property", HttpStatus.PARTIAL_CONTENT);
    }
}
