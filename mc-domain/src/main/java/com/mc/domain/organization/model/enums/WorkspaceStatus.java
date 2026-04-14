package com.mc.domain.organization.model.enums;

/**
 * WorkspaceStatus — Domain Enum (Organization Context)
 *
 * <p>Represents the lifecycle state of a Workspace.
 * Using an enum rather than a raw String enforces at compile-time that the status
 * can only be one of these known values — invalid strings such as "aktiv" or "1"
 * are impossible to assign.</p>
 */
public enum WorkspaceStatus {

    /**
     * The workspace is in normal operation and visible to members.
     */
    ACTIVE,

    /**
     * The workspace has been soft-deleted and is no longer visible in the main UI.
     * Data is retained for audit/recovery purposes.
     */
    DELETED
}
