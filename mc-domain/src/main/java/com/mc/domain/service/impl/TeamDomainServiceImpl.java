package com.mc.domain.service.impl;

import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.*;
import com.mc.domain.model.enums.RoleType;
import com.mc.domain.repository.*;
import com.mc.domain.service.TeamDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamDomainServiceImpl implements TeamDomainService {

    private final TeamRepository teamRepository;
    private final ApartmentRepository apartmentRepository;
    private final ApartmentMemberRepository apartmentMemberRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    public Apartment saveApartment(User user, Team team, String name, String description, String backgroundUrl) {

        Apartment apartment = new Apartment();
        apartment.setName(name);
        apartment.setDescription(description);
        apartment.setBackgroundUrl(backgroundUrl);
        apartment.setOwner(user);
        apartment.setTeam(team);
        apartmentRepository.save(apartment);

        Role apartmentRole = roleRepository.findByName(RoleType.ROLE_OWNER_APARTMENT.name())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleType.ROLE_OWNER_APARTMENT.name()));

        ApartmentMember apartmentMember = new ApartmentMember();
        apartmentMember.setApartment(apartment);
        apartmentMember.setUser(user);
        apartmentMember.setRole(apartmentRole);
        apartmentMemberRepository.save(apartmentMember);

        return apartment;
    }

    @Override
    @Transactional
    public List<User> addApartmentMember(List<User> users, Apartment apartment) {

        List<Long> userIds = users.stream().map(User::getId).toList();

        List<Long> existingMemberUserIds = apartmentMemberRepository.findAllUserIdsByApartmentIdAndUserIdsIn(apartment.getId(), userIds);

        Role memberRole = roleRepository.findByName(RoleType.ROLE_MEMBER_APARTMENT.name())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleType.ROLE_MEMBER_APARTMENT.name()));

        Role ownerRole = roleRepository.findByName(RoleType.ROLE_OWNER_APARTMENT.name())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleType.ROLE_OWNER_APARTMENT.name()));

        long apartmentMembers = apartmentMemberRepository.countByApartmentId(apartment.getId());

        List<ApartmentMember> newMembers = users.stream()
                .filter(user -> !existingMemberUserIds.contains(user.getId()))
                .map(user -> {
                    ApartmentMember member = new ApartmentMember();
                    member.setApartment(apartment);
                    member.setUser(user);
                    if (apartmentMembers == 0) {
                        member.setRole(ownerRole);
                    } else {
                        member.setRole(memberRole);
                    }
                    return member;
                })
                .toList();

        if (!newMembers.isEmpty()) {
            apartmentMemberRepository.saveAllApartmentMembers(newMembers);
        }

        return userRepository.findAllByApartmentId(apartment.getId());

    }

    @Override
    public List<User> deletedApartmentMember(ApartmentMember apartmentMember) {
        apartmentMemberRepository.deleteApartmentMember(apartmentMember);
        return userRepository.findAllByApartmentId(apartmentMember.getApartment().getId());
    }

    @Override
    public boolean assignApartmentOwner(boolean isOwner, Long userId, Long apartmentId) {

        ApartmentMember apartmentMember = apartmentMemberRepository.findApartmentMemberByUserIdAndApartmentId(userId, apartmentId)
                .orElseThrow(() -> new ResourceNotFoundException("ApartmentMember", "id", apartmentId.toString()));

        apartmentMember.setOwner(isOwner);
        apartmentMemberRepository.save(apartmentMember);

        return isOwner;

    }

}
