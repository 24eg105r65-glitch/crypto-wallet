package com.cryptovault.controller;

import com.cryptovault.dto.PasswordChangeDto;
import com.cryptovault.dto.UserUpdateDto;
import com.cryptovault.entity.User;
import com.cryptovault.security.CustomUserDetails;
import com.cryptovault.service.AuthSessionService;
import com.cryptovault.service.NotificationService;
import com.cryptovault.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    private final UserService userService;
    private final AuthSessionService authSessionService;
    private final NotificationService notificationService;

    public ProfileController(UserService userService,
                             AuthSessionService authSessionService,
                             NotificationService notificationService) {
        this.userService = userService;
        this.authSessionService = authSessionService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public String profilePage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long userId = userDetails.getId();
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!model.containsAttribute("updateDto")) {
            UserUpdateDto updateDto = UserUpdateDto.builder()
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .build();
            model.addAttribute("updateDto", updateDto);
        }

        if (!model.containsAttribute("passwordDto")) {
            model.addAttribute("passwordDto", new PasswordChangeDto());
        }

        model.addAttribute("user", user);
        model.addAttribute("sessions", authSessionService.getUserSessions(userId));
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userId));
        model.addAttribute("pageTitle", "Account & Security Settings — CryptoVault");

        return "profile/index";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute("updateDto") UserUpdateDto updateDto,
                                 BindingResult bindingResult,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please provide valid profile details.");
            return "redirect:/profile";
        }

        try {
            userService.updateProfile(userDetails.getId(), updateDto);
            redirectAttributes.addFlashAttribute("successMessage", "Profile information updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("passwordDto") PasswordChangeDto passwordDto,
                                 BindingResult bindingResult,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 8 characters long.");
            return "redirect:/profile";
        }

        try {
            userService.changePassword(userDetails.getId(), passwordDto);
            redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully. Next logins will require the new password.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/profile";
    }

    @PostMapping("/sessions/revoke/{id}")
    public String revokeSession(@PathVariable("id") Long sessionId,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        authSessionService.revokeSession(sessionId, userDetails.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Session revoked successfully.");
        return "redirect:/profile";
    }
}
