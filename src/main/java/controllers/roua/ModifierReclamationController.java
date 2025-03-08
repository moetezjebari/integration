package controllers.roua;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Mission;
import models.Reclamation;
import models.User;
import services.ReclamationService;
import utils.BadWordFilter;

public class ModifierReclamationController {

    @FXML
    private TextField titreTextField; // Le titre reste un TextField
    @FXML
    private TextArea descriptionTextArea; // Remplacement de descriptionTextField par TextArea

    private final ReclamationService reclamationService = new ReclamationService();
    private Reclamation reclamation;
    private User utilisateur; // Pour stocker l'utilisateur
    private Mission mission; // Pour stocker la mission

    // Méthode appelée par MainFX pour passer les données
    public void initData(Reclamation reclamation) {
        if (reclamation != null) {
            this.reclamation = reclamation;
            titreTextField.setText(reclamation.getTitre());
            descriptionTextArea.setText(reclamation.getDescription()); // Utilisation de TextArea
        }
    }
    // Méthode pour définir l'utilisateur
    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }

    // Méthode pour définir la mission
    public void setMission(Mission mission) {
        this.mission = mission;
    }

    // Modifier la réclamation

    @FXML
    public void modifierReclamation() {
        String titre = titreTextField.getText().trim();
        String description = descriptionTextArea.getText().trim(); // Utilisation de TextArea

        // Vérifiez que tous les champs sont remplis
        if (titre.isEmpty() || description.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        // 🔥 Filtrage des bad words pour le titre et la description
        String titreFiltree = BadWordFilter.filterBadWords(titre);
        String descriptionFiltree = BadWordFilter.filterBadWords(description);

        // Mettre à jour les champs de la réclamation
        reclamation.setTitre(titreFiltree);
        reclamation.setDescription(descriptionFiltree);

        try {
            reclamationService.modifier(reclamation); // Appel au service pour modifier la réclamation
            showInfo("Réclamation modifiée avec succès !");
            closeWindow(); // Fermer la fenêtre après la modification
        } catch (Exception e) {
            showError("Erreur lors de la modification : " + e.getMessage());
        }
    }

    // Annuler la modification et fermer la fenêtre
    @FXML
    public void annulerReclamation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Annulation");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir annuler ?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                closeWindow(); // Fermer la fenêtre si l'utilisateur confirme
            }
        });
    }

    // Fermer la fenêtre
    private void closeWindow() {
        Stage stage = (Stage) titreTextField.getScene().getWindow();
        stage.close();
    }

    // Afficher une alerte d'erreur
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Afficher une alerte d'information
    private void showInfo(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
