package com.mc.application.organization.service.impl;

import com.mc.application.organization.dto.request.CreateApartmentRequest;
import com.mc.application.organization.dto.request.UpdateApartmentRequest;
import com.mc.application.organization.dto.response.ApartmentResponse;
import com.mc.application.organization.service.ApartmentAppService;
import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.organization.model.entity.Apartment;
import com.mc.domain.organization.port.OrgUserContextPort;
import com.mc.domain.organization.port.OrgUserView;
import com.mc.domain.organization.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ApartmentAppServiceImpl — Application Service (Organization Context)
 *
 * <p><strong>INVARIANTS MOVED TO DOMAIN:</strong>
 * The previous implementation used {@code new Apartment()} followed by 6 setter calls —
 * any combination of these could produce an invalid entity state. Now, all creation
 * invariants live in {@link Apartment#create(String, String, String, UUID, UUID, UUID)}.
 * This service is a pure orchestrator: resolve user → call domain → persist → map DTO.</p>
 *
 * <p><strong>SECURITY FIX:</strong>
 * The old controller accepted {@code @RequestParam UUID currentUserId} which allowed
 * any authenticated user to create an apartment as any other user. Now the owner is
 * resolved from the JWT-backed security context via {@link OrgUserContextPort}.</p>
 */
@Service
@RequiredArgsConstructor
public class ApartmentAppServiceImpl implements ApartmentAppService {

    private final ApartmentRepository apartmentRepository;
    private final OrgUserContextPort orgUserContextPort;

    @Override
    @Transactional
    public ApartmentResponse createApartment(CreateApartmentRequest request) {
        // Resolve creator from security context — the owner is whoever is authenticated
        OrgUserView currentUser = orgUserContextPort.getCurrentUser();

        // Domain factory enforces: name non-blank, ownerId/teamId/workspaceId non-null
        Apartment apartment = Apartment.create(
                request.getName(),
                request.getDescription(),
                request.getBackgroundUrl(),
                currentUser.id(),        // ownerId = authenticated user
                request.getTeamId(),
                request.getWorkspaceId()
        );

        Apartment saved = apartmentRepository.save(apartment);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ApartmentResponse getApartmentById(UUID id) {
        return apartmentRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApartmentResponse> getAllApartmentsByWorkspaceId(UUID workspaceId) {
        return apartmentRepository.findAllByWorkspaceId(workspaceId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ApartmentResponse updateApartment(UUID id, UpdateApartmentRequest request) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", "id", id));

        // Delegate update to the domain entity's behavior method — invariants enforced there
        apartment.updateDetails(request.getName(), request.getDescription(), request.getBackgroundUrl());
        Apartment updated = apartmentRepository.save(apartment);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteApartment(UUID id) {
        // Resolve deleter from security context for audit trail
        OrgUserView currentUser = orgUserContextPort.getCurrentUser();

        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", "id", id));

        // Domain entity enforces deletion invariant (already-deleted guard)
        apartment.softDelete(currentUser.id());
        apartmentRepository.save(apartment);
    }

    private ApartmentResponse toResponse(Apartment apartment) {
        return new ApartmentResponse(
                apartment.getId(),
                apartment.getName(),
                apartment.getDescription(),
                apartment.getBackgroundUrl(),
                apartment.getOwnerId(),
                apartment.getTeamId(),
                apartment.getWorkspaceId()
        );
    }
}
