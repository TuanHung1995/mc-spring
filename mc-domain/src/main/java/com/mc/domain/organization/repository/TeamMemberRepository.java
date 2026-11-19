package com.mc.domain.organization.repository;

import com.mc.domain.organization.model.entity.TeamMember;

import java.util.UUID;

public interface TeamMemberRepository {

    void addTeamMember(TeamMember teamMember);

}
