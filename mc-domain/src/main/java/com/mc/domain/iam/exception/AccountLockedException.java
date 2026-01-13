package com.mc.domain.iam.exception;

import com.mc.domain.exception.DomainException;

/**
 * Exception thrown when a user account is locked (e.g., too many failed login attempts).
 */
public class AccountLockedException extends DomainException {
    
    public AccountLockedException() {
        super("Account is locked. Please contact support or use the unlock link.");
    }

    public AccountLockedException(String message) {
        super(message);
    }
}
