package controllers;

import models.Mission;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.MissionService;

public class ModifierMissionController {
    @FXML
    private TextField titreField; // Champ pour le titre de la mission

    @FXML
    private TextArea descriptionArea; // Zone de texte pour la description de la mission

    private Mission missionToUpdate; // Mission à modifier
    private MissionService missionService = new MissionService(); // Service pour gérer les opérations sur les missions

    /**
     * Méthode pour définir la mission à modifier.
     * Cette méthode est appelée depuis AfficherListeMissionController.
     *
     * @param mission La mission à modifier.
     */
    public void setMissionToUpdate(Mission mission) {
        this.missionToUpdate = mission;
        // Pré-remplir les champs avec les données de la mission
        titreField.setText(mission.getTitre());
        descriptionArea.setText(mission.getDescription());
    }

    /**
     * Méthode pour valider la modification de la mission.
     * Cette méthode est appelée lorsque l'utilisateur clique sur le bouton "Valider".
     */
    @FXML
    public void validerModification() {
        // Récupérer les nouvelles valeurs saisies par l'utilisateur
        String nouveauTitre = titreField.getText();
        String nouvelleDescription = descriptionArea.getText();

        // Vérifier que les champs ne sont pas vides
        if (nouveauTitre.isEmpty() || nouvelleDescription.isEmpty()) {
            afficherAlerte("Erreur", "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Mettre à jour l'objet Mission avec les nouvelles valeurs
        missionToUpdate.setTitre(nouveauTitre);
        missionToUpdate.setDescription(nouvelleDescription);

        // Appeler le service pour mettre à jour la mission dans la base de données
        boolean miseAJourReussie = missionService.update(missionToUpdate);

        if (miseAJourReussie) {
            afficherAlerte("Succès", "Mission mise à jour", "La mission a été mise à jour avec succès.");
            fermerFenetre();
        } else {
            afficherAlerte("Erreur", "Échec de la mise à jour", "La mission a été mise à jour avec succès.");
        }
    }

    /**
     * Méthode pour annuler la modification et fermer la fenêtre.
     * Cette méthode est appelée lorsque l'utilisateur clique sur le bouton "Annuler".
     */
    @FXML
    public void annulerModification() {
        fermerFenetre();
    }

    /**
     * Méthode utilitaire pour afficher une alerte.
     *
     * @param titre     Le titre de l'alerte.
     * @param entete    L'en-tête de l'alerte.
     * @param contenu   Le contenu de l'alerte.
     */
    private void afficherAlerte(String titre, String entete, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    /**
     * Méthode utilitaire pour fermer la fenêtre de modification.
     */
    private void fermerFenetre() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }
}