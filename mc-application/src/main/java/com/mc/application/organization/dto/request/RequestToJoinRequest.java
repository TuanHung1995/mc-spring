package com.mc.application.organization.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * RequestToJoinRequest — Application DTO
 *
 * <p>Submitted by any authenticated user who wishes to join an apartment.
 * The requesting user is resolved from the JWT security context.
 * Creates a PENDING membership that an apartment owner must approve.</p>
 */
@Data
public class RequestToJoinRequest {

    @NotNull(message = "apartmentId is required")
    private UUID apartmentId;
}
