package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.ApartmentMember;
import com.mc.domain.model.entity.User;
import com.mc.domain.repository.ApartmentMemberRepository;
import com.mc.infrastructure.persistence.mapper.ApartmentMemberJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApartmentMemberInfrasRepositoryImpl implements ApartmentMemberRepository {

    private final ApartmentMemberJPAMapper apartmentMemberJPAMapper;

    @Override
    public void save(ApartmentMember apartmentMember) {
        apartmentMemberJPAMapper.save(apartmentMember);
    }

    @Override
    public void deleteByUserIdAndApartmentId(Long userId, Long apartmentId) {
        apartmentMemberJPAMapper.deleteByUserIdAndApartmentId(userId, apartmentId);
    }

    @Override
    public void deleteApartmentMember(ApartmentMember apartmentMember) {
        apartmentMemberJPAMapper.deleteById(apartmentMember.getId());
    }

    @Override
    public Optional<ApartmentMember> findApartmentMemberByUserIdAndApartmentId(Long userId, Long apartmentId) {
        return apartmentMemberJPAMapper.findByUserIdAndApartmentId(userId, apartmentId);
    }

    @Override
    public List<Long> findAllUserIdsByApartmentIdAndUserIdsIn(Long apartmentId, List<Long> userIds) {
        return apartmentMemberJPAMapper.findUserIdsByApartmentIdAndUserIdsIn(apartmentId, userIds);
    }

    @Override
    public long countByApartmentId(Long apartmentId) {
        return apartmentMemberJPAMapper.countByApartmentId(apartmentId);
    }

    @Override
    public List<User> saveAllApartmentMembers(List<ApartmentMember> apartmentMembers) {
        return apartmentMemberJPAMapper.saveAll(apartmentMembers).stream()
                .map(ApartmentMember::getUser)
                .toList();
    }

}
