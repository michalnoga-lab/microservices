package com.app.controllers;

import com.app.data.AppData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app-git-data")
public class AppDataController {

    private final AppData appData;

    @GetMapping
    public String getData() {
        return appData.getMessage() + " VERSION: " + appData.getVersion();
    }
}