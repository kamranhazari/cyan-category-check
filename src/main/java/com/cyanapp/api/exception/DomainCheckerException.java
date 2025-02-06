package com.cyanapp.api.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class DomainCheckerException extends RuntimeException {

    private String message;

    public DomainCheckerException(String message) {
        super(message);
        this.message = message;
    }
}
