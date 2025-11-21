package com.mc.domain.service.impl;

import com.mc.domain.repository.TestRepository;
import com.mc.domain.service.TestDomainService;
import org.springframework.stereotype.Service;

@Service
public class TestDomainServiceImpl implements TestDomainService {

    private final TestRepository testRepository;

    public TestDomainServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public String testRepository(String text) {
        return testRepository.testRepository(text);
    }
}
