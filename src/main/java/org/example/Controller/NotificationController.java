package org.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.Model.Notification;
import org.example.Service.NotificationService;
import java.util.List;

public class NotificationController {
    @FXML private VBox notificationsListContainer;
    NotificationService notificationService = new NotificationService();
    // ID de l'utilisateur actuellement connecté (à adapter selon votre système d'authentification)
    private int currentUserId = 1;

    public void initialize() {
        loadNotifications();
    }


    private void loadNotifications() {
        // Récupérer les notifications de l'utilisateur connecté
        List<Notification> notifications = notificationService.getNotificationsByUser(currentUserId);

        notificationsListContainer.getChildren().clear();

        if (notifications.isEmpty()) {
            Label emptyLabel = new Label("Aucune notification");
            emptyLabel.setStyle("-fx-text-fill: #888; -fx-font-style: italic;");
            notificationsListContainer.getChildren().add(emptyLabel);
            return;
        }

        for (Notification notification : notifications) {
            // Créer un élément de notification
            HBox notificationItem = createNotificationItem(notification);
            notificationsListContainer.getChildren().add(notificationItem);
        }
    }

    private HBox createNotificationItem(Notification notification) {
        HBox container = new HBox(10);
        container.getStyleClass().add("notification-item");
        if (!notification.isVu()) {
            container.getStyleClass().add("unread");
        }

        VBox content = new VBox(5);

        Label titleLabel = new Label(notification.getTitle());
        titleLabel.getStyleClass().add("notification-title");

        Label messageLabel = new Label(notification.getMessage());
        messageLabel.getStyleClass().add("notification-message");
        messageLabel.setWrapText(true);

        Label timeLabel = new Label(notification.getTimestamp());
        timeLabel.getStyleClass().add("notification-time");

        content.getChildren().addAll(titleLabel, messageLabel, timeLabel);
        container.getChildren().add(content);

        // Marquer comme lu au clic
        container.setOnMouseClicked(event -> {
            notificationService.markAsRead(notification.getId());
            container.getStyleClass().remove("unread");
        });

        return container;
    }

    @FXML
    private void markAllAsRead() {
        // Récupérer toutes les notifications et les marquer comme lues
        List<Notification> notifications = notificationService.getNotificationsByUser(currentUserId);
        for (Notification notification : notifications) {
            notificationService.markAsRead(notification.getId());
        }

        // Recharger les notifications
        loadNotifications();
    }
}