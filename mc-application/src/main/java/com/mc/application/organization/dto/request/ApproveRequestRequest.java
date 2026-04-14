package com.mc.application.organization.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * ApproveRequestRequest — Application DTO
 *
 * <p>Submitted by an apartment owner to approve or reject a PENDING membership.
 * The caller (owner) is resolved from the JWT security context.
 * The membership is identified by its UUID ({@code membershipId})
 * — not by userId — for an unambiguous reference.</p>
 *
 * <p>When {@code approve = true}: membership becomes ACTIVE.
 * When {@code approve = false}: membership becomes REJECTED.</p>
 */
@Data
public class ApproveRequestRequest {

    @NotNull(message = "membershipId is required")
    private UUID membershipId;

    /** true = approve the membership; false = reject it */
    private boolean approve;
}
