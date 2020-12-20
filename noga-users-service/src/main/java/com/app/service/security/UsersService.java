package com.app.service.security;

import com.app.dto.CreateUserDto;
import com.app.dto.GetUserDto;
import com.app.exception.AppUsersServiceException;
import com.app.mapper.Mappers;
import com.app.repository.UsersRepository;
import com.app.validator.CreateUserDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public Long registerUser(CreateUserDto createUserDto) {
        var createUserDtoValidator = new CreateUserDtoValidator();
        var errors = createUserDtoValidator.validate(createUserDto);
        if (!errors.isEmpty()) {
            var message = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(Collectors.joining(", "));
            throw new AppUsersServiceException(message);
        }

        if (usersRepository.findByUsername(createUserDto.getUsername()).isPresent()) {
            throw new AppUsersServiceException("User already in DB");
        }

        var user = Mappers.fromCreateUserDtoToUser(createUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return usersRepository
                .save(user)
                .getId();
    }

    public GetUserDto findByUsername(String username) {
        if (username == null) {
            throw new AppUsersServiceException("username is null");
        }

        return usersRepository
                .findByUsername(username)
                .map(Mappers::fromUserToGetUserDto)
                .orElseThrow(() -> new AppUsersServiceException("Cannot find user with username"));
    }

    public GetUserDto findByUserId(Long id) {
        return usersRepository
                .findById(id)
                .map(Mappers::fromUserToGetUserDto)
                .orElseThrow(() -> new AppUsersServiceException("Cannot find user with ID: " + id));
    }
}
