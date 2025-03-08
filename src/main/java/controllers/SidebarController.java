package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SidebarController {

    @FXML private Button btnHome;
    @FXML private Button voirmission;
    @FXML private Button voircondidature;
    @FXML private Button avis;
    @FXML private Button reclamation;

    @FXML private Button profileButton;
    @FXML private ImageView profileImage;

    @FXML
    private void initialize() {
        URL imageUrl = getClass().getResource("/images/profile.png");
        if (imageUrl != null) {
            profileImage.setImage(new Image(imageUrl.toExternalForm()));
        } else {
            System.err.println("⚠️ Profile image not found: /images/profile.png");
        }

        btnHome.setOnAction(event -> loadView("/GPublication/fxml/MainView.fxml"));
        voirmission.setOnAction(event -> loadView("/views/AfficherListeMission.fxml"));
        voircondidature.setOnAction(event -> loadView("/views/AfficherListeCandidature.fxml"));
        avis.setOnAction(event -> loadView("/AvisView.fxml"));
        reclamation.setOnAction(event -> loadView("/AfficherReclamation.fxml"));
        profileButton.setOnAction(event -> openProfileView());
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader.getLocation() == null) {
                throw new IOException("FXML file not found: " + fxmlPath);
            }
            Parent root = loader.load();
            Stage stage = (Stage) btnHome.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Error loading FXML file: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void openProfileView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/edit_profile.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("FXML file not found: /admin/edit_profile.fxml");
            }
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Profile");
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Error loading edit_profile.fxml");
            e.printStackTrace();
        }
    }

}
