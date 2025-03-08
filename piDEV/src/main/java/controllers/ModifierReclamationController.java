package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.Reclamation;
import services.ReclamationService;

public class ModifierReclamationController {

    @FXML
    private TextField titreTextField, descriptionTextField;

    private final ReclamationService reclamationService = new ReclamationService();
    private Reclamation reclamation;

    // Méthode appelée par MainFX pour passer les données
    public void initData(Reclamation reclamation) {
        if (reclamation != null) {
            this.reclamation = reclamation;
            titreTextField.setText(reclamation.getTitre());
            descriptionTextField.setText(reclamation.getDescription());
        }
    }

    // Modifier la réclamation
    @FXML
    public void modifierReclamation() {
        String titre = titreTextField.getText().trim();
        String description = descriptionTextField.getText().trim();

        if (titre.isEmpty() || description.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        reclamation.setTitre(titre);
        reclamation.setDescription(description);

        try {
            reclamationService.modifier(reclamation);
            showInfo("Réclamation modifiée avec succès !");
            closeWindow();
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
                closeWindow();
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
