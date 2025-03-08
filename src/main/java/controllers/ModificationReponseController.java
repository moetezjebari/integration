package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Reponse;
import services.ReponseService;
import utils.BadWordFilter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class ModificationReponseController {

    @FXML
    private TextArea textAreaContenu;

    @FXML
    private ImageView imageView; // ImageView pour afficher l'image

    @FXML
    private Button btnModifier, btnFermer, btnChangerImage; // Boutons

    private Reponse reponse;
    private ReponseService reponseService;
    private byte[] imageData; // Stocke l'image sous forme de bytes

    public void initialize() {
        reponseService = new ReponseService();
    }

    public void setReponse(Reponse reponse) {
        this.reponse = reponse;
        textAreaContenu.setText(reponse.getContenu());

        // Charger l'image actuelle si elle existe
        if (reponse.getImageData() != null) {
            imageData = reponse.getImageData();
            Image image = new Image(new ByteArrayInputStream(imageData));
            imageView.setImage(image);
        } else {
            imageView.setImage(null);
        }
    }
    @FXML
    private void modifierReponse() {
        String nouveauContenu = textAreaContenu.getText();

        if (nouveauContenu.isEmpty()) {
            afficherAlerte("Attention", "Le contenu de la réponse ne peut pas être vide.");
            return;
        }

        // 🔥 Filtrage des bad words pour le contenu de la réponse
        String contenuFiltre = BadWordFilter.filterBadWords(nouveauContenu);

        reponse.setContenu(contenuFiltre); // Utiliser le contenu filtré
        reponse.setImageData(imageData); // Mettre à jour l'image si elle a été modifiée

        try {
            reponseService.modifier(reponse);
            afficherAlerte("Succès", "Réponse modifiée avec succès !");
            fermer();
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de la modification de la réponse : " + e.getMessage());
        }
    }

    @FXML
    private void choisirNouvelleImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());

        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                imageData = fis.readAllBytes(); // Lire l'image en bytes
                imageView.setImage(new Image(selectedFile.toURI().toString())); // Mettre à jour l'ImageView
            } catch (IOException e) {
                afficherAlerte("Erreur", "Impossible de charger l'image : " + e.getMessage());
            }
        }
    }

    @FXML
    private void fermer() {
        Stage stage = (Stage) textAreaContenu.getScene().getWindow();
        stage.close();
    }


    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
