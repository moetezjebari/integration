/*package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.Reclamation;
import models.User; // Assurez-vous d'importer le modèle User
import models.Mission; // Assurez-vous d'importer le modèle Mission
import services.ReclamationService;
import java.sql.SQLException;
import java.util.Date;

public class AjouterReclamationControllers {

    @FXML
    private TextField titreTextField, descriptionTextField;

    private ReclamationService reclamationService = new ReclamationService();
    private User utilisateur; // Assurez-vous que cet utilisateur est correctement initialisé ailleurs
    private Mission mission; // Assurez-vous que cette mission est également initialisée

    // Ajouter une nouvelle réclamation
    @FXML
    public void ajouterReclamation() {
        String titre = titreTextField.getText();
        String description = descriptionTextField.getText();

        // Vérifiez que l'utilisateur et la mission sont définis
        if (utilisateur == null) {
            showError("Utilisateur non défini. Veuillez vous connecter.");
            return;
        }

        if (mission == null) {
            showError("Mission non définie. Veuillez sélectionner une mission.");
            return;
        }

        if (titre.isEmpty() || description.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        Reclamation nouvelleReclamation = new Reclamation();
        nouvelleReclamation.setUser(utilisateur); // Liaison de l'utilisateur à la réclamation
        nouvelleReclamation.setDescription(description);
        nouvelleReclamation.setMission(mission); // Liaison de la mission à la réclamation
        nouvelleReclamation.setTitre(titre);
        nouvelleReclamation.setDate(new Date()); // Date actuelle
        nouvelleReclamation.setStatus("En attente"); // Vous pouvez définir un statut par défaut

        try {
            reclamationService.ajouter(nouvelleReclamation);
            showInfo("Réclamation ajoutée !");
            closeWindow();
        } catch (Exception e) {
            showError("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
        }
    }

    // Annuler l'ajout de la réclamation
    @FXML
    public void annulerReclamation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Annulation");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir annuler l'ajout ?");
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

    // Méthode pour définir l'utilisateur
    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }

    // Méthode pour définir la mission
    public void setMission(Mission mission) {
        this.mission = mission;
    }
}*/
package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.Reclamation;
import models.User;
import models.Mission;
import services.ReclamationService;
import java.util.Date;

public class AjouterReclamationControllers {

    @FXML
    private TextField titreTextField, descriptionTextField;

    private ReclamationService reclamationService = new ReclamationService();
    private User utilisateur; // Utilisateur passé à partir de la méthode d'ouverture
    private Mission mission; // Mission passée à partir de la méthode d'ouverture

    // Méthode pour définir l'utilisateur
    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }

    // Méthode pour définir la mission
    public void setMission(Mission mission) {
        this.mission = mission;
    }

    // Ajouter une nouvelle réclamation
    @FXML
    public void ajouterReclamation() {
        String titre = titreTextField.getText();
        String description = descriptionTextField.getText();

        // Vérifiez que l'utilisateur et la mission sont définis
        if (utilisateur == null) {
            showError("Utilisateur non défini. Veuillez vous connecter.");
            return;
        }

        if (mission == null) {
            showError("Mission non définie. Veuillez sélectionner une mission.");
            return;
        }

        if (titre.isEmpty() || description.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        Reclamation nouvelleReclamation = new Reclamation();
        nouvelleReclamation.setUser(utilisateur); // Liaison de l'utilisateur à la réclamation
        nouvelleReclamation.setDescription(description);
        nouvelleReclamation.setMission(mission); // Liaison de la mission à la réclamation
        nouvelleReclamation.setTitre(titre);
        nouvelleReclamation.setDate(new Date()); // Date actuelle
        nouvelleReclamation.setStatus("En attente"); // Statut par défaut

        try {
            reclamationService.ajouter(nouvelleReclamation);
            showInfo("Réclamation ajoutée !");
            closeWindow();
        } catch (Exception e) {
            showError("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
        }
    }

    // Annuler l'ajout de la réclamation
    @FXML
    public void annulerReclamation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Annulation");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir annuler l'ajout ?");
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



