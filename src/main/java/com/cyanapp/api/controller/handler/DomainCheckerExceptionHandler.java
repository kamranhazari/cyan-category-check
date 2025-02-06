package com.cyanapp.api.controller.handler;


import com.cyanapp.api.exception.DomainCheckerException;
import com.cyanapp.api.model.response.GeneralExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DomainCheckerExceptionHandler {

    @ExceptionHandler(DomainCheckerException.class)
    public ResponseEntity<GeneralExceptionResponse> handleValidationExceptions(
            DomainCheckerException exp) {
        GeneralExceptionResponse response = new GeneralExceptionResponse();
        response.setMessage(exp.getMessage());
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GeneralExceptionResponse> handleValidationExceptions(IllegalArgumentException exp) {
        GeneralExceptionResponse response = new GeneralExceptionResponse();
        response.setMessage(exp.getMessage());
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GeneralExceptionResponse> handleValidationExceptions(RuntimeException exp) {
        GeneralExceptionResponse response = new GeneralExceptionResponse();
        response.setMessage(exp.getMessage());
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
