package com.cryptovault.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUpdateDto {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    private String phoneNumber;

    public UserUpdateDto() {}

    public UserUpdateDto(String fullName, String email, String phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static UserUpdateDtoBuilder builder() {
        return new UserUpdateDtoBuilder();
    }

    public static class UserUpdateDtoBuilder {
        private String fullName;
        private String email;
        private String phoneNumber;

        public UserUpdateDtoBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserUpdateDtoBuilder email(String email) { this.email = email; return this; }
        public UserUpdateDtoBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }

        public UserUpdateDto build() {
            return new UserUpdateDto(fullName, email, phoneNumber);
        }
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
