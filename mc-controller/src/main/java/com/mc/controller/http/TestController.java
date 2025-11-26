package com.mc.controller.http;

import com.mc.application.service.TestAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/")
@Slf4j
public class TestController {

    private final TestAppService testAppService;

    public TestController(TestAppService testAppService) {
        this.testAppService = testAppService;
    }

    @GetMapping("hello")
    public String hello() {
        log.info("TestController: hello endpoint called");
        return testAppService.testRepository("hello");
    }

    @GetMapping ("user")
    public Principal getUser(Principal user) {
        log.info("TestController: user endpoint called");
        return user;
    }

    @GetMapping
    public String root() {
        log.info("TestController: root endpoint called");
        return "Welcome to the application root!";
    }
}
