package fr.athlaes.services.ord.infrastructure.adapter.rest.exceptions;

import org.antlr.v4.runtime.atn.ErrorInfo;
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
    // TODO : Complete classes

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> notFoundHandler(HttpServletRequest request, ResourceNotFoundException ex) {
        String errorMessage = String.format("Resource with id : %s is not found", ex.getId());
        return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
