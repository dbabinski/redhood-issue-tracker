package com.redhood.issuetracker.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "jwt")
public class JWTPropertiesConfiguration {

    private String secret;
    private String tokenValidity;
    private String tokenValidityForRememberMe;

    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public String getTokenValidity() {
        return tokenValidity;
    }
    public void setTokenValidity(String tokenValidity) {
        this.tokenValidity = tokenValidity;
    }

    public String getTokenValidityForRememberMe() {
        return tokenValidityForRememberMe;
    }

    public void setTokenValidityForRememberMe(String tokenValidityForRememberMe) {
        this.tokenValidityForRememberMe = tokenValidityForRememberMe;
    }
}
