package com.mc.domain.iam.exception;

import com.mc.domain.exception.DomainException;

/**
 * Exception thrown when a token (refresh, reset, etc.) has expired.
 */
public class TokenExpiredException extends DomainException {
    
    public TokenExpiredException() {
        super("Token has expired");
    }

    public TokenExpiredException(String tokenType) {
        super(tokenType + " has expired");
    }
}
