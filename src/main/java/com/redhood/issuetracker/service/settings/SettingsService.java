package com.redhood.issuetracker.service.settings;

import com.redhood.issuetracker.repository.settings.entity.Settings;
import com.redhood.issuetracker.repository.settings.repository.SettingsRepository;
import com.redhood.issuetracker.service.account.accounts.AccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SettingsService {
    //------------------------------------------------------------------------------------------------------------------
    // Fields
    //------------------------------------------------------------------------------------------------------------------
    private final Logger log = LoggerFactory.getLogger(SettingsService.class);
    private final SettingsRepository settingsRepository;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------------------------------
    @Autowired
    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Methods
    //------------------------------------------------------------------------------------------------------------------
    public Settings findSettings() {
        Optional<Settings> settings = settingsRepository.findById(1L);
        return settings.get();
    }

}
