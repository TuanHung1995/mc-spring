package com.mc.domain.service.impl;

import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.*;
import com.mc.domain.model.enums.RoleType;
import com.mc.domain.repository.*;
import com.mc.domain.service.TeamDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<User> addApartmentMember(User user, Apartment apartment) {

        ApartmentMember newMember = new ApartmentMember();
        newMember.setApartment(apartment);
        newMember.setUser(user);

        apartmentMemberRepository.save(newMember);

        return userRepository.findAllByApartmentId(apartment.getId());

    }

    @Override
    public List<User> deletedApartmentMember(ApartmentMember apartmentMember) {
        apartmentMemberRepository.deleteApartmentMember(apartmentMember);
        return userRepository.findAllByApartmentId(apartmentMember.getApartment().getId());
    }

}
