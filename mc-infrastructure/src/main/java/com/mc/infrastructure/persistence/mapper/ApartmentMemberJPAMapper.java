package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.ApartmentMember;
import com.mc.domain.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApartmentMemberJPAMapper extends JpaRepository<ApartmentMember, Long> {

    void deleteByUserIdAndApartmentId(Long userId, Long apartmentId);

    Optional<ApartmentMember> findByUserIdAndApartmentId(Long userId, Long apartmentId);

    @Query("SELECT am.user.id FROM ApartmentMember am WHERE am.apartment.id = :apartmentId AND am.user.id IN :userIds")
    List<Long> findUserIdsByApartmentIdAndUserIdsIn(@Param("apartmentId") Long apartmentId, @Param("userIds") List<Long> userIds);

    long countByApartmentId(Long apartmentId);

    @Query("SELECT am.role FROM ApartmentMember am WHERE am.apartment.id = :apartmentId AND am.user.id = :userId")
    Optional<Role> findRoleByApartmentIdAndUserId(@Param("apartmentId") Long apartmentId, @Param("userId") Long userId);

    @Query("SELECT am.user.email FROM ApartmentMember am WHERE am.apartment.id = :apartmentId AND am.isOwner = true")
    List<String> findOwnerEmailsByApartmentId(@Param("apartmentId") Long apartmentId);

}
