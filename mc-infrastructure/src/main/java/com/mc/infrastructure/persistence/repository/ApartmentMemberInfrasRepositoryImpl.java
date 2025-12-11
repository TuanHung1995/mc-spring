package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.ApartmentMember;
import com.mc.domain.repository.ApartmentMemberRepository;
import com.mc.infrastructure.persistence.mapper.ApartmentMemberJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApartmentMemberInfrasRepositoryImpl implements ApartmentMemberRepository {

    private final ApartmentMemberJPAMapper apartmentMemberJPAMapper;

    @Override
    public void save(ApartmentMember apartmentMember) {
        apartmentMemberJPAMapper.save(apartmentMember);
    }

}
