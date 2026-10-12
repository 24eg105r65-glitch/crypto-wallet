package com.cryptovault.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegisterDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain alphanumeric characters, dots, underscores, and hyphens")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;

    public UserRegisterDto() {}

    public UserRegisterDto(String username, String email, String fullName, String phoneNumber, String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public static UserRegisterDtoBuilder builder() {
        return new UserRegisterDtoBuilder();
    }

    public static class UserRegisterDtoBuilder {
        private String username;
        private String email;
        private String fullName;
        private String phoneNumber;
        private String password;
        private String confirmPassword;

        public UserRegisterDtoBuilder username(String username) { this.username = username; return this; }
        public UserRegisterDtoBuilder email(String email) { this.email = email; return this; }
        public UserRegisterDtoBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserRegisterDtoBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public UserRegisterDtoBuilder password(String password) { this.password = password; return this; }
        public UserRegisterDtoBuilder confirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; return this; }

        public UserRegisterDto build() {
            return new UserRegisterDto(username, email, fullName, phoneNumber, password, confirmPassword);
        }
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
