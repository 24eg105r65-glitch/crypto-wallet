package com.cryptovault.controller;

import com.cryptovault.security.CustomUserDetails;
import com.cryptovault.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public String listNotifications(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long userId = userDetails.getId();
        model.addAttribute("notifications", notificationService.getUserNotifications(userId));
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userId));
        model.addAttribute("pageTitle", "Security & System Notifications — CryptoVault");
        return "notifications/index";
    }

    @PostMapping("/read/{id}")
    public String markAsRead(@PathVariable("id") Long id,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.markAsRead(id, userDetails.getId());
        return "redirect:/notifications";
    }

    @PostMapping("/read-all")
    public String markAllAsRead(@AuthenticationPrincipal CustomUserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        notificationService.markAllAsRead(userDetails.getId());
        redirectAttributes.addFlashAttribute("successMessage", "All notifications marked as read.");
        return "redirect:/notifications";
    }
}
