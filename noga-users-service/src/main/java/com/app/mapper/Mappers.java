package com.app.mapper;

import com.app.dto.CreateUserDto;
import com.app.dto.GetUserDto;
import com.app.model.security.User;

public interface Mappers {
    static User fromCreateUserDtoToUser(CreateUserDto createUserDto) {
        return User
                .builder()
                .username(createUserDto.getUsername())
                .password(createUserDto.getPassword())
                .role(createUserDto.getRole())
                .build();
    }

    static GetUserDto fromUserToGetUserDto(User user) {
        return GetUserDto
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }
}