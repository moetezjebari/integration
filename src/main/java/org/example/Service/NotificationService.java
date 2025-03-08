package org.example.Service;

import org.example.Model.Notification;
import org.example.Util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import java.sql.Connection;

public class NotificationService {

    private final Connection cnx2;

    public NotificationService() {
        cnx2 = DatabaseConnection.getInstance().getCnx();
    }

    public void pushNotification(Notification notification){
        String query = "INSERT INTO notifications (destinataire_user_id, source_user_id , title , message ,timestamp, vu , type , related_item_id) " +
                "VALUES (?,?,?,?,NOW(),?,?,?)";
        try{
            PreparedStatement ps = cnx2.prepareStatement(query);
            ps.setInt(1, notification.getDestinataire_user_id());
            ps.setInt(2, notification.getSource_user_id());
            ps.setString(3, notification.getTitle());
            ps.setString(4, notification.getMessage());
            ps.setBoolean(5, notification.isVu());
            ps.setString(6, notification.getType());
            ps.setInt(7, notification.getRelated_item_id());

            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public  List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notification";
        try {
            PreparedStatement ps = cnx2.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                notifications.add(new Notification(
                        rs.getInt("destinataire_user_id"),
                        rs.getInt("source_user_id"),
                        rs.getString("title"),
                        rs.getString("message"),
                        rs.getBoolean("vu"),
                        rs.getString("type"),
                        rs.getInt("related_item_id")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return notifications;
    }

    public  List<Notification> getNotificationsByUser(int currentUserId)  {
        String query = "SELECT * FROM notifications WHERE destinataire_user_id = ?";
        List<Notification> notifications = new ArrayList<>();
try {
    PreparedStatement ps = cnx2.prepareStatement(query);
    ps.setInt(1, currentUserId);
    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
        notifications.add(new Notification(
                rs.getInt("id"),
                rs.getInt("destinataire_user_id"),
                rs.getInt("source_user_id"),
                rs.getString("title"),
                rs.getString("message"),
                rs.getString("timestamp"),
                rs.getBoolean("vu"),
                rs.getString("type"),
                rs.getInt("related_item_id")
        ));
    }
}catch(Exception e){
    e.printStackTrace();
}
        return notifications;
    }

    public void markAsRead(int notificationId) {
        String query = "UPDATE notifications SET read = true WHERE id = ?";
        try {
            PreparedStatement ps = cnx2.prepareStatement(query);
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
