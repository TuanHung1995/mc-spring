package com.mc.domain.service;

import com.mc.domain.model.entity.Apartment;
import com.mc.domain.model.entity.ApartmentMember;
import com.mc.domain.model.entity.Team;
import com.mc.domain.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface TeamDomainService {

    Optional<Team> findById(Long id);

    Apartment saveApartment(User user, Team team, String name, String description, String backgroundUrl);

    List<User> addApartmentMember(List<User> user, Apartment apartment);

    List<User> deletedApartmentMember(ApartmentMember apartmentMember);

    boolean assignApartmentOwner(boolean isOwner, Long userId, Long apartmentId);

    void requestToJoinApartment(Long userId, Long apartmentId);

}
