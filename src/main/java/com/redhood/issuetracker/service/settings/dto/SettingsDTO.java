package com.redhood.issuetracker.service.settings.dto;

public class SettingsDTO {

    private Long id;

    private Long tokenValidityName;

    public SettingsDTO() {
    }

    public SettingsDTO(Long id, Long tokenValidityName) {
        this.id = id;
        this.tokenValidityName = tokenValidityName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTokenValidityName() {
        return tokenValidityName;
    }

    public void setTokenValidityName(Long tokenValidityName) {
        this.tokenValidityName = tokenValidityName;
    }

    @Override
    public String toString() {
        return "SettingsDTO{" +
                "id=" + id +
                ", tokenValidityName='" + tokenValidityName + '\'' +
                '}';
    }
}
