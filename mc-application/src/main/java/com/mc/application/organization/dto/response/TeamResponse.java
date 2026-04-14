package com.mc.application.organization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * TeamResponse — Application DTO (Organization Context)
 *
 * <p>The outbound representation of a Team exposed to HTTP clients.
 * Using a DTO here (instead of returning the domain entity directly) ensures:
 * <ol>
 *   <li>The Team aggregate's internal model can evolve without breaking the API contract.</li>
 *   <li>Sensitive or implementation-specific fields (e.g., deletedBy, deletedAt) are never
 *       accidentally serialized to the client.</li>
 * </ol>
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponse {
    private UUID id;
    private String name;
    private String slug;
    private String description;
}
