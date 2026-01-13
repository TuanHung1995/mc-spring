package com.mc.domain.iam.model.vo;

import com.mc.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;

/**
 * Email Value Object - Pure Domain (no JPA dependencies)
 * Self-validating, immutable value object representing an email address.
 */
@Getter
@EqualsAndHashCode
public class Email {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    private final String value;

    public Email(String value) {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new DomainException("Invalid email format: " + value);
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

