package com.redhood.issuetracker.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JWTPropertiesConfiguration.class)
public class PropertiesConfiguration {

    private JWTPropertiesConfiguration jwtPropertiesConfiguration;

    public PropertiesConfiguration(JWTPropertiesConfiguration jwtPropertiesConfiguration) {
        this.jwtPropertiesConfiguration = jwtPropertiesConfiguration;
    }
}
