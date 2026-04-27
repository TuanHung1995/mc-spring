package com.mc.controller.organization.http;

import com.mc.application.organization.dto.response.TeamResponse;
import com.mc.application.organization.service.TeamAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * TeamController — HTTP Adapter (Organization Context)
 *
 * <p>Exposes Team management endpoints. Returns {@link TeamResponse} DTOs
 * — never raw domain entities (which previously leaked internal state to the wire).</p>
 */
@RestController
@RequestMapping("/api/v1/org/teams")
@RequiredArgsConstructor
@Component("orgTeamController")
public class TeamController {

    @Qualifier("orgTeamAppService")
    private final TeamAppService teamAppService;

    /**
     * Creates a new team.
     *
     * <p>Accepts name and ownerId as query params. In a production system,
     * ownerId should be resolved from the security context — consider wiring
     * {@link com.mc.domain.organization.port.OrgUserContextPort} here
     * when this endpoint is fully secured.</p>
     */
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(
            @RequestParam String name,
            @RequestParam UUID ownerId) {
        return ResponseEntity.ok(teamAppService.createTeam(name, ownerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable UUID id) {
        return teamAppService.getTeamById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID id) {
        teamAppService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
