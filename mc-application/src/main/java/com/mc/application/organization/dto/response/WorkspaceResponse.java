package com.mc.application.organization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * WorkspaceResponse — Application DTO (Organization Context)
 *
 * <p>Outbound representation of a Workspace returned by the API.
 * Uses UUID-typed IDs to match the refactored org_workspaces schema.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceResponse {
    private UUID id;
    private String name;
    private String status;
    private UUID teamId;
}
