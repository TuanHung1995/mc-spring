package com.mc.infrastructure.persistence.repository;

import com.mc.domain.repository.TestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestRepositoryInfrasImpl implements TestRepository {

    @Override
    public String testRepository(String text) {
        return "Hi from Infrastructure: " + text;
    }
}
