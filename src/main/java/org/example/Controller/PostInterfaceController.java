package org.example.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class PostInterfaceController {

    @FXML
    private ImageView profileImage;
    @FXML
    private Button mediaButton;
    @FXML
    private TextField text;

    public void initialize() {
        // Créer un cercle de même taille que l'image
        Circle clip = new Circle(profileImage.getFitWidth() / 2, profileImage.getFitHeight() / 2, profileImage.getFitWidth() / 2);
        profileImage.setClip(clip);  // Appliquer le cercle comme "clip" pour l'image

        // Optionnel : ajouter une bordure (si nécessaire)
        profileImage.setStyle("-fx-border-color: black; -fx-border-width: 2;");
    }

    public void handleButtonClick() {

        try {
            // Charger la nouvelle scène depuis le fichier FXML (home.fxml)
            Parent newRoot = FXMLLoader.load(getClass().getResource("/GPublication/fxml/Post.fxml"));
            Scene newScene = new Scene(newRoot );

            // Changer la scène actuelle
            // Stage stage = (Stage) mediaButton.getScene().getWindow();
            Stage stage = new Stage();
            stage.setScene(newScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Vérifiez la console pour des détails d'erreur
        }
    }

}
