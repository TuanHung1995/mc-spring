package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.ApartmentMember;
import com.mc.domain.model.entity.Role;
import com.mc.domain.port.MailSender;
import com.mc.domain.repository.ApartmentMemberRepository;
import com.mc.infrastructure.persistence.mapper.ApartmentMemberJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApartmentMemberInfrasRepositoryImpl implements ApartmentMemberRepository {

    private final ApartmentMemberJPAMapper apartmentMemberJPAMapper;
    private final MailSender mailSender;

    private String apartmentApiUrl = "/api/v1/teams/";

    @Value("${server.port}")
    private String serverPort;

    @Value("${constants.development}")
    private boolean isDevelopment;

    @Value("${constants.frontend}")
    private String appUrl;

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
    public void saveAllApartmentMembers(List<ApartmentMember> apartmentMembers) {
        apartmentMemberJPAMapper.saveAll(apartmentMembers);
    }

    @Override
    public Optional<Role> findRoleByApartmentIdAndUserId(Long apartmentId, Long userId) {
        return apartmentMemberJPAMapper.findRoleByApartmentIdAndUserId(apartmentId, userId);
    }

    @Override
    public List<String> findOwnerEmailsByApartmentId(Long apartmentId) {
        return apartmentMemberJPAMapper.findOwnerEmailsByApartmentId(apartmentId);
    }

}
