package com.cryptovault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordChangeDto {

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 100, message = "New password must be at least 8 characters long")
    private String newPassword;

    @NotBlank(message = "Please confirm your new password")
    private String confirmNewPassword;

    public PasswordChangeDto() {}

    public PasswordChangeDto(String currentPassword, String newPassword, String confirmNewPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public static PasswordChangeDtoBuilder builder() {
        return new PasswordChangeDtoBuilder();
    }

    public static class PasswordChangeDtoBuilder {
        private String currentPassword;
        private String newPassword;
        private String confirmNewPassword;

        public PasswordChangeDtoBuilder currentPassword(String currentPassword) { this.currentPassword = currentPassword; return this; }
        public PasswordChangeDtoBuilder newPassword(String newPassword) { this.newPassword = newPassword; return this; }
        public PasswordChangeDtoBuilder confirmNewPassword(String confirmNewPassword) { this.confirmNewPassword = confirmNewPassword; return this; }

        public PasswordChangeDto build() {
            return new PasswordChangeDto(currentPassword, newPassword, confirmNewPassword);
        }
    }

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    public String getConfirmNewPassword() { return confirmNewPassword; }
    public void setConfirmNewPassword(String confirmNewPassword) { this.confirmNewPassword = confirmNewPassword; }
}
