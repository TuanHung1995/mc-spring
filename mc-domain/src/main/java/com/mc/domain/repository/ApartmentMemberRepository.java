package com.mc.domain.repository;

import com.mc.domain.model.entity.ApartmentMember;

import java.util.Optional;

public interface ApartmentMemberRepository {

    void save(ApartmentMember apartmentMember);

    void deleteByUserIdAndApartmentId(Long userid, Long apartmentId);

    void deleteApartmentMember(ApartmentMember apartmentMember);

    Optional<ApartmentMember> findApartmentMemberByUserIdAndApartmentId(Long userId, Long apartmentId);
}
