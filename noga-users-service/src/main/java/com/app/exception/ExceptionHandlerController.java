package com.app.exception;

import com.app.dto.ErrorDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(AppUsersServiceException.class)
    public ErrorDto handleAppUsersException(AppUsersServiceException appUsersServiceException) {
        return ErrorDto
                .builder()
                .message(appUsersServiceException.getMessage())
                .build();
    }
}