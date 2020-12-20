package com.app.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Component
@ConfigurationProperties("app-data")
public class AppData {

    private String message;
    private Integer version;
}