package com.app.dto;

import com.app.model.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserDto {
    private String username;
    private String password;
    private String passwordConfirmation;
    private Role role;
}