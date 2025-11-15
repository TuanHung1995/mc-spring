package com.mc.controller.http;

import com.mc.application.service.TestAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    private final TestAppService testAppService;

    public TestController(TestAppService testAppService) {
        this.testAppService = testAppService;
    }

    @GetMapping("/hello")
    public String hello() {
        log.info("TestController: hello endpoint called");
        return testAppService.testRepository("hello");
    }
}
