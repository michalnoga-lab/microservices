package com.app.exceptions;

import com.app.dto.ErrorDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(AppServiceException.class)
    public ErrorDto handleAppServiceException(AppServiceException appServiceException) {
        return ErrorDto.builder()
                .message(appServiceException.getMessage())
                .build();
    }
}