package com.mc.application.service.impl;

import com.mc.application.service.TestAppService;
import com.mc.domain.service.TestDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestAppServiceImpl implements TestAppService {

    private final TestDomainService testDomainService;

    public TestAppServiceImpl(TestDomainService testDomainService) {
        this.testDomainService = testDomainService;
    }

    @Override
    public String testRepository(String text) {
        return testDomainService.testRepository(text);
    }
}
