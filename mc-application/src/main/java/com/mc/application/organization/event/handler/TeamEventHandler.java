package com.mc.application.organization.event.handler;

import com.mc.application.organization.service.TeamAppService;
import com.mc.domain.core.event.broker.rabbitMQ.RegisterIntegrationEvent;
import com.mc.domain.organization.port.in.TeamWorkerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamEventHandler implements TeamWorkerPort {

    private final TeamAppService teamAppService;

    @Override
    public void processUserRegistration(RegisterIntegrationEvent event) {
        log.info("Processing user registration for team with userId: {}", event.getUserId());
        teamAppService.createTeam(event.getUserId().toString() , event.getUserId());
    }

}
