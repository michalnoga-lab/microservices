package com.app.controller;

import com.app.dto.GetUserDto;
import com.app.service.security.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/find")
public class FindUsersController {

    private final UsersService usersService;

    @GetMapping("/name/{username}")
    public GetUserDto findByName(@PathVariable String username) {
        return usersService.findByUsername(username);
    }

    @GetMapping("/id/{id}")
    public GetUserDto findById(@PathVariable Long id) {
        return usersService.findByUserId(id);
    }
}