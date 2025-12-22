package com.mc.domain.repository;

import com.mc.domain.model.entity.ApartmentMember;
import com.mc.domain.model.entity.Role;

import java.util.List;
import java.util.Optional;

public interface ApartmentMemberRepository {

    void save(ApartmentMember apartmentMember);

    void deleteByUserIdAndApartmentId(Long userid, Long apartmentId);

    void deleteApartmentMember(ApartmentMember apartmentMember);

    Optional<ApartmentMember> findApartmentMemberByUserIdAndApartmentId(Long userId, Long apartmentId);

    List<Long> findAllUserIdsByApartmentIdAndUserIdsIn(Long apartmentId, List<Long> userIds);

    long countByApartmentId(Long apartmentId);

    void saveAllApartmentMembers(List<ApartmentMember> apartmentMembers);

    Optional<Role> findRoleByApartmentIdAndUserId(Long apartmentId, Long userId);

    List<String> findOwnerEmailsByApartmentId(Long apartmentId);

}
