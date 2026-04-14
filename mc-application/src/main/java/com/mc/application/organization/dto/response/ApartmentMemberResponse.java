package com.mc.application.organization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ApartmentMemberResponse — Application DTO (Organization Context)
 *
 * <p>The outbound representation of an ApartmentMember returned by member management APIs.
 * Never exposes the domain entity directly — this DTO is what gets serialized to JSON.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentMemberResponse {
    private UUID id;
    private UUID apartmentId;
    private UUID userId;
    private Long roleId;
    private boolean owner;
    private String status;
    private LocalDateTime joinedAt;
}
