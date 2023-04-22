package com.redhood.issuetracker.service.account.dto;

public class PasswordChangeDTO {

    private static final long serialVersionUID = 1L;

    private String currentPassword;

    private String newPassword;

    public PasswordChangeDTO() {
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
