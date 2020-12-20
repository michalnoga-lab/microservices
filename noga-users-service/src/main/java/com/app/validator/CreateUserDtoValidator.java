package com.app.validator;

import com.app.dto.CreateUserDto;
import com.app.exception.AppUsersServiceException;
import com.app.repository.UsersRepository;
import com.app.validator.generic.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CreateUserDtoValidator implements Validator<CreateUserDto> {

    @Override
    public Map<String, String> validate(CreateUserDto createUserDto) {

        var errors = new HashMap<String, String>();

        if (createUserDto == null) {
            errors.put("create user dto", "create user dto is null");
            return errors;
        }

        if (createUserDto.getUsername() == null) {
            errors.put("username", "username is null");
        }

        if (createUserDto.getRole() == null) {
            errors.put("role", "role is null");
        }

        if (!Objects.equals(createUserDto.getPassword(), createUserDto.getPasswordConfirmation())) {
            errors.put("password", "passwords are not correct");
        }
        return errors;
    }
}