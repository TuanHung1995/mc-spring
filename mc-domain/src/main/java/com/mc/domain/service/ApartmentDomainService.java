package com.mc.domain.service;

import com.mc.domain.model.entity.Apartment;
import com.mc.domain.model.entity.ApartmentMember;

import java.util.List;
import java.util.Optional;

public interface ApartmentDomainService {

    Optional<Apartment> findApartmentById(Long apartmentId);

    void deleteTeam(Long id);

    Optional<ApartmentMember> findApartmentMemberByUserIdAndApartmentId(Long userId, Long apartmentId);

    /**
     * Updates the information of an existing apartment.
     * <p>
     * This method performs a partial update. Only fields present in the request object
     * will be updated. It also enforces security checks to ensure only authorized
     * users (OWNER role) can perform updates.
     * </p>
     *
     * @param apartmentId the unique identifier of the apartment to update
     * @param requesterId the unique identifier of the user requesting the update
     * @param apartmentName      the new name of the apartment (optional)
     * @param description the new description of the apartment (optional)
     * @param avatarUrl   the URL of the new avatar image (optional)
     * @param backgroundImageUrl the URL of the new background cover image (optional)
     * @param isPrivate   the visibility status of the apartment (optional)
     * @throws com.mc.domain.exception.ResourceNotFoundException if the apartment or user membership is not found
     * @throws com.mc.domain.exception.BusinessLogicException    if the user does not have the OWNER role
     */
    void updateApartment(Long apartmentId, Long requesterId, String apartmentName, String description, String avatarUrl, String backgroundImageUrl, Boolean isPrivate);

    /**
     * Retrieves all apartments associated with a specific workspace ID.
     *
     * @param workspaceId the unique identifier of the workspace
     * @return a list of Apartment entities belonging to the specified workspace
     */
    List<Apartment> getAllByWorkspaceId(Long workspaceId);

    Apartment getApartmentById(Long apartmentId);

}
