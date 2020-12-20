package com.app.proxy;

import com.app.dto.ErrorDto;
import com.app.dto.GetUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "api-gateway-find-user", name = "noga-users-service")
public interface FindUserProxy {

    @GetMapping("/find/name/{username}")
    GetUserDto findByName(@PathVariable("username") String username);

    @GetMapping("/find/id/{id}")
    GetUserDto findById(@PathVariable("id") Long id);

    @GetMapping("/find/user")
    ErrorDto helloFromUser();

    @GetMapping("/find/admin")
    ErrorDto helloFromAdmin();
}