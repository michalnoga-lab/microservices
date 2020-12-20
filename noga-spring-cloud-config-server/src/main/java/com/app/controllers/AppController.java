package com.app.controllers;

import com.app.data.AppData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app-data")
public class AppController {

    private final AppData appData;

    @GetMapping
    public String getConfig() {
        return appData.getMessage() + "\nVERSION: " + appData.getVersion();
    }
}