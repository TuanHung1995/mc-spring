package com.mc.domain.iam.exception;

import com.mc.domain.exception.DomainException;

/**
 * Exception thrown when a user is not found in the system.
 */
public class UserNotFoundException extends DomainException {
    
    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException withId(Object userId) {
        return new UserNotFoundException("User not found with ID: " + userId);
    }

    public static UserNotFoundException withEmail(String email) {
        return new UserNotFoundException("User not found with email: " + email);
    }
}
