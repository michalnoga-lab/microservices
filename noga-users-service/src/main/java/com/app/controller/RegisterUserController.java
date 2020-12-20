package com.app.controller;

import com.app.dto.CreateUserDto;
import com.app.service.security.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security")
public class RegisterUserController {
    private final UsersService usersService;

    @PostMapping
    public Long registerUser(@RequestBody CreateUserDto createUserDto) {
        return usersService.registerUser(createUserDto);
    }
}
