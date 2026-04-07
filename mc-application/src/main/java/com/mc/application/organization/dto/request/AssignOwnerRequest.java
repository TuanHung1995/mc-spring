package com.mc.application.organization.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * AssignOwnerRequest — Application DTO
 *
 * <p>Submitted by the current apartment owner to transfer ownership to another
 * ACTIVE member. The caller's identity (current owner) is resolved from the JWT
 * security context. After this operation, the caller is no longer owner
 * and the {@code newOwnerId} user becomes the sole owner.</p>
 */
@Data
public class AssignOwnerRequest {

    @NotNull(message = "apartmentId is required")
    private UUID apartmentId;

    @NotNull(message = "newOwnerId is required")
    private UUID newOwnerId;
}
