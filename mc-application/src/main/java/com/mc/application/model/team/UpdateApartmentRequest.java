package com.mc.application.model.team;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for updating apartment details.
 * Contains optional fields; null fields indicate no change to the corresponding entity property.
 */
@Getter
@Setter
public class UpdateApartmentRequest {

    /**
     * The new name of the apartment.
     * Must be between 1 and 100 characters if provided.
     */
    @Size(min = 1, max = 100, message = "Apartment name must be between 1 and 100 characters")
    private String name;

    /**
     * The new description of the apartment.
     * Maximum length is 500 characters.
     */
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    /**
     * The URL of the new avatar image.
     */
    private String avatarUrl;

    /**
     * The URL of the new background cover image.
     */
    private String backgroundImageUrl;

    /**
     * The visibility status of the apartment (e.g., true for private, false for public).
     */
    private Boolean isPrivate;
}