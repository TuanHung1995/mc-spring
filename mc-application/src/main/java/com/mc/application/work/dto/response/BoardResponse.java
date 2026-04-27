package com.mc.application.work.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * BoardResponse — Application DTO (Work Context)
 *
 * <p>Outbound representation of a Board. Never exposes JPA entities or domain models to the HTTP layer.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {
    private UUID id;
    private String name;
    private String description;
    private String type;
    private String purpose;
    private UUID workspaceId;
    private UUID createdById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
