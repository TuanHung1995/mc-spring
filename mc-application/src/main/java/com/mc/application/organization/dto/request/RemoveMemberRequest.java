package com.mc.application.organization.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * RemoveMemberRequest — Application DTO
 *
 * <p>Submitted by an apartment owner to remove a specific user from the apartment.
 * The caller (owner) is resolved from the JWT security context.
 * The owner cannot remove themselves — a guard is enforced in the application service.</p>
 */
@Data
public class RemoveMemberRequest {

    @NotNull(message = "apartmentId is required")
    private UUID apartmentId;

    @NotNull(message = "userId of the member to remove is required")
    private UUID userId;
}
