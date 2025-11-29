package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.Role;
import com.mc.domain.model.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeamMemberJPAMapper extends JpaRepository<TeamMember, Long> {

    @Query("SELECT tm.role FROM TeamMember tm WHERE tm.team.id = :teamId AND tm.user.id = :userId")
    Optional<Role> findRoleByTeamIdAndUserId(Long teamId, Long userId);

}
