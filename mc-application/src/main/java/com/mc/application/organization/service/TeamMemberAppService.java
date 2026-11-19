package com.mc.application.organization.service;

import java.util.UUID;

public interface TeamMemberAppService {

    void addTeamMember(UUID userId, UUID teamId, Long roleId);

}
