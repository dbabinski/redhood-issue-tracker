package com.redhood.issuetracker.repository.settings.repository;

import com.redhood.issuetracker.repository.settings.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
}
