package com.app.controllers;

import com.app.exceptions.AppServiceException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @GetMapping("/user")
    @HystrixCommand(fallbackMethod = "testUserFallback")
    public String testUser() {
        if (Math.random() > 0.5) {
            throw new AppServiceException("ERROR in hello from user");
        }
        return "Hello from user";
    }

    @GetMapping("/admin")
    @HystrixCommand(fallbackMethod = "testAdminFallback")
    public String testAdmin() {
        if (Math.random() > 0.5) {
            throw new AppServiceException("ERROR in hello from admin");
        }
        return "Hello from admin";
    }

    public String testUserFallback() {
        return "Hello from user fallback method";
    }

    public String testAdminFallback() {
        return "Hello from admin fallback method";
    }
}