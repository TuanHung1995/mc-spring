package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamJPAMapper extends JpaRepository<Team, Long> {

//    Team save(Team team);

}
