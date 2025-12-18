package com.mc.domain.repository;

import com.mc.domain.model.entity.ApartmentMember;
import com.mc.domain.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface ApartmentMemberRepository {

    void save(ApartmentMember apartmentMember);

    void deleteByUserIdAndApartmentId(Long userid, Long apartmentId);

    void deleteApartmentMember(ApartmentMember apartmentMember);

    Optional<ApartmentMember> findApartmentMemberByUserIdAndApartmentId(Long userId, Long apartmentId);

    List<Long> findAllUserIdsByApartmentIdAndUserIdsIn(Long apartmentId, List<Long> userIds);

    long countByApartmentId(Long apartmentId);

    List<User> saveAllApartmentMembers(List<ApartmentMember> apartmentMembers);

}
