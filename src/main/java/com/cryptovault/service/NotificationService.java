package com.cryptovault.service;

import com.cryptovault.entity.Notification;
import com.cryptovault.entity.NotificationType;
import com.cryptovault.entity.User;

import java.util.List;

public interface NotificationService {
    List<Notification> getUserNotifications(Long userId);
    List<Notification> getRecentNotifications(Long userId);
    long getUnreadCount(Long userId);
    void markAsRead(Long notificationId, Long userId);
    void markAllAsRead(Long userId);
    Notification sendNotification(User user, String title, String message, NotificationType type, String refUrl);
}
