package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import models.Reclamation;
import services.ReclamationService;
import javafx.stage.Stage;
import main.MainFX; // Importer MainFX pour ouvrir les fenêtres
import javafx.scene.control.ButtonType;
import java.sql.SQLException;
import models.User;
import models.Mission;

public class AfficherReclamationController {

    @FXML
    private ListView<Reclamation> reclamationListView;

    @FXML
    private Button ajouterButton, modifierButton, supprimerButton;

    private ReclamationService reclamationService = new ReclamationService();

    private User utilisateur; // Ajoutez un attribut pour l'utilisateur
    private Mission mission; // Ajoutez un attribut pour la mission

    @FXML
    public void initialize() {
        chargerReclamations();
    }

    // Méthode pour définir l'utilisateur
    public void setUser(User utilisateur) {
        this.utilisateur = utilisateur;
    }

    // Méthode pour définir la mission
    public void setMission(Mission mission) {
        this.mission = mission;
    }

    // Charger les réclamations depuis la base de données
    private void chargerReclamations() {
        try {
            reclamationListView.setItems(FXCollections.observableArrayList(reclamationService.recuperer()));
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des réclamations : " + e.getMessage());
        }
    }

    // Ouvrir la fenêtre d'ajout de réclamation
    @FXML
    public void ajouterReclamation() {
        try {
            MainFX.openAjouterReclamationWindow((Stage) ajouterButton.getScene().getWindow(), utilisateur, mission); // Passez l'utilisateur et la mission
            chargerReclamations(); // Rafraîchir après ajout
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de la réclamation : " + e.getMessage());
        }
    }

    // Ouvrir la fenêtre de modification d'une réclamation
    @FXML
    public void modifierReclamation() {
        Reclamation selectedReclamation = reclamationListView.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            try {
                MainFX.openModifierReclamationWindow((Stage) modifierButton.getScene().getWindow(), selectedReclamation);
                chargerReclamations(); // Rafraîchir après modification
            } catch (Exception e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture de la fenêtre de modification : " + e.getMessage());
            }
        } else {
            afficherAlerte(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner une réclamation à modifier.");
        }
    }

    // Supprimer une réclamation
    @FXML
    public void supprimerReclamation() {
        Reclamation selectedReclamation = reclamationListView.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette réclamation ?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        reclamationService.supprimer(selectedReclamation);
                        chargerReclamations(); // Rafraîchir après suppression
                        afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Réclamation supprimée avec succès !");
                    } catch (SQLException e) {
                        afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de la réclamation : " + e.getMessage());
                    }
                }
            });
        } else {
            afficherAlerte(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner une réclamation à supprimer.");
        }
    }

    // Afficher des alertes d'information ou d'erreur
    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
