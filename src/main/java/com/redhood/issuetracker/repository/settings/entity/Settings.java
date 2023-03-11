package com.redhood.issuetracker.repository.settings.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "settings", schema = "public")
public class Settings {

    @Id
    @SequenceGenerator(name = "public.settings_id_gen", sequenceName = "public.settings_id_gen", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "token_validity_time")
    private Long tokenValidityTime;

    public Settings() {
    }

    public Settings(Long id, Long tokenValidityTime) {
        this.id = id;
        this.tokenValidityTime = tokenValidityTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTokenValidityTime() {
        return tokenValidityTime;
    }

    public void setTokenValidityTime(Long tokenValidityName) {
        this.tokenValidityTime = tokenValidityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return Objects.equals(id, settings.id) && Objects.equals(tokenValidityTime, settings.tokenValidityTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tokenValidityTime);
    }

    @Override
    public String toString() {
        return "Settings{" +
                "id=" + id +
                ", tokenValidityTime='" + tokenValidityTime + '\'' +
                '}';
    }
}
