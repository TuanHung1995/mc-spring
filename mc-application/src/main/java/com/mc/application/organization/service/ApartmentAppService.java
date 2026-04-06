package com.mc.application.organization.service;

import com.mc.application.organization.dto.request.CreateApartmentRequest;
import com.mc.application.organization.dto.request.UpdateApartmentRequest;
import com.mc.application.organization.dto.response.ApartmentResponse;

import java.util.List;
import java.util.UUID;

/**
 * ApartmentAppService — Application Service Port (Organization Context)
 *
 * <p>All IDs are UUID. The authenticated user is resolved internally via
 * {@code OrgUserContextPort} — callers never pass {@code currentUserId} explicitly.</p>
 */
public interface ApartmentAppService {

    /** Creates a new apartment. Creator resolved from security context. */
    ApartmentResponse createApartment(CreateApartmentRequest request);

    ApartmentResponse getApartmentById(UUID id);

    List<ApartmentResponse> getAllApartmentsByWorkspaceId(UUID workspaceId);

    ApartmentResponse updateApartment(UUID id, UpdateApartmentRequest request);

    void deleteApartment(UUID id);
}
