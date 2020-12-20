package com.app.dto;

import com.app.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserDto {
    private Long id;
    private String username;
    private String password;
    private Role role;
}