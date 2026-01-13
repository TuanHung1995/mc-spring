package com.mc.domain.iam.exception;

import com.mc.domain.exception.DomainException;

/**
 * Exception thrown when a token is invalid or malformed.
 */
public class InvalidTokenException extends DomainException {
    
    public InvalidTokenException() {
        super("Invalid or malformed token");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
