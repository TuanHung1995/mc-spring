package com.mc.controller.organization.http;

import com.mc.application.organization.dto.request.CreateApartmentRequest;
import com.mc.application.organization.dto.request.UpdateApartmentRequest;
import com.mc.application.organization.dto.response.ApartmentResponse;
import com.mc.application.organization.service.ApartmentAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * ApartmentController — HTTP Adapter (Organization Context)
 *
 * <p><strong>SECURITY FIX:</strong>
 * The previous version accepted {@code @RequestParam Long currentUserId} —
 * a client could supply any user's ID and create/delete apartments as that user.
 * The owner is now resolved from the JWT security context inside the application service.</p>
 */
@RestController
@RequestMapping("/api/v1/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentAppService apartmentAppService;

    @PostMapping
    public ResponseEntity<ApartmentResponse> createApartment(
            @Valid @RequestBody CreateApartmentRequest request) {
        // Owner resolved from JWT security context — not from request params
        return ResponseEntity.ok(apartmentAppService.createApartment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentResponse> getApartment(@PathVariable UUID id) {
        return ResponseEntity.ok(apartmentAppService.getApartmentById(id));
    }

    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<ApartmentResponse>> getApartmentsByWorkspace(
            @PathVariable UUID workspaceId) {
        return ResponseEntity.ok(apartmentAppService.getAllApartmentsByWorkspaceId(workspaceId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApartmentResponse> updateApartment(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateApartmentRequest request) {
        return ResponseEntity.ok(apartmentAppService.updateApartment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable UUID id) {
        apartmentAppService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }
}
