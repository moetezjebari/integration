package controllers.roua;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import models.Reclamation;
import models.Reponse;
import models.User; // Use User instead of Utilisateur
import services.ReponseService;
import utils.BadWordFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class AjoutReponseController {

    @FXML
    private TextArea textAreaContenu;

    @FXML
    private ImageView imageView;

    private Reclamation reclamation;
    private User user; // Changed from Utilisateur to User
    private final ReponseService reponseService = new ReponseService();
    private byte[] imageData;

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }

    public void setUser(User user) { // Changed method name and type to User
        this.user = user;
    }

    @FXML
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());

        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                imageData = fis.readAllBytes();
                imageView.setImage(new Image(selectedFile.toURI().toString()));
            } catch (IOException e) {
                afficherAlerte("Erreur", "Impossible de charger l'image : " + e.getMessage());
            }
        }
    }

    @FXML
    private void ajouterReponse() {
        String contenu = textAreaContenu.getText();
        if (contenu.isEmpty() || imageData == null) {
            afficherAlerte("Attention", "Le contenu de la réponse ne peut pas être vide et une image doit être sélectionnée.");
            return;
        }

        String contenuFiltre = BadWordFilter.filterBadWords(contenu);

        try {
            Reponse nouvelleReponse = new Reponse(reclamation, user, contenuFiltre, imageData, new Date()); // Now using User instead of Utilisateur
            reponseService.ajouter(nouvelleReponse);

            afficherAlerte("Succès", "Réponse ajoutée avec succès !");
            fermer();
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de l'ajout de la réponse : " + e.getMessage());
        }
    }

    @FXML
    private void fermer() {
        imageView.getScene().getWindow().hide();
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
