package com.mc.application.organization.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * AddMemberRequest — Application DTO
 *
 * <p>Used by an apartment owner to directly invite a known user (by UUID)
 * into the apartment. The invited user's membership starts as PENDING.</p>
 *
 * <p>The requesting user (the owner) is resolved from the JWT security context —
 * it is NOT a field in this request body to prevent privilege escalation.</p>
 */
@Data
public class AddMemberRequest {

    @NotNull(message = "apartmentId is required")
    private UUID apartmentId;

    @NotNull(message = "userId is required")
    private UUID userId;

    /**
     * The role to assign to the invited member.
     * Long because the roles table still uses Long PK (legacy).
     * If null, the infrastructure default member role will be assigned.
     */
    private Long roleId;
}
