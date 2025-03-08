package controllers.roua;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Mission;
import models.Reclamation;
import models.User;
import services.MailService;
import services.MissionService;
import services.ReclamationService;
import utils.BadWordFilter;
import java.util.Date;
import java.util.List;

public class AjouterReclamationControllers {

    @FXML
    private TextField titreTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private ComboBox<Mission> missionComboBox;

    private ReclamationService reclamationService = new ReclamationService();
    private MissionService missionService = new MissionService();
    private User utilisateur; // Utilisateur passé à partir de la méthode d'ouverture

    @FXML
    public void initialize() {
        loadMissions(); // Load missions into ComboBox
        setDefaultUser(); // Ensure a user is set
        setUtilisateur(this.utilisateur);
    }

    private void setDefaultUser() {
        if (this.utilisateur == null) {
            this.utilisateur = new User(1, "Nom", "Prénom", "email@domain.com", "password", "freelance");
        }
    }

    private void loadMissions() {
        List<Mission> missions = missionService.getAllMissions();

        if (missions.isEmpty()) {
            System.out.println("❌ Aucune mission récupérée !");
        } else {
            System.out.println("✅ Missions récupérées : " + missions.size());
        }

        ObservableList<Mission> missionList = FXCollections.observableArrayList(missions);
        missionComboBox.setItems(missionList);
    }

    @FXML
    public void ajouterReclamation() {
        String titre = titreTextField.getText();
        String description = descriptionTextArea.getText();
        Mission selectedMission = missionComboBox.getValue();

        if (utilisateur == null) {
            showError("Utilisateur non défini. Veuillez vous connecter.");
            return;
        }

        if (selectedMission == null) {
            showError("Veuillez sélectionner une mission.");
            return;
        }

        if (titre.isEmpty() || description.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        String titreFiltre = BadWordFilter.filterBadWords(titre);
        String descriptionFiltre = BadWordFilter.filterBadWords(description);

        Reclamation nouvelleReclamation = new Reclamation();
        nouvelleReclamation.setUser(utilisateur);
        nouvelleReclamation.setDescription(descriptionFiltre);
        nouvelleReclamation.setMission(selectedMission);
        nouvelleReclamation.setTitre(titreFiltre);
        nouvelleReclamation.setDate(new Date());
        nouvelleReclamation.setStatus("En attente");

        try {
            // Save the reclamation
            reclamationService.ajouter(nouvelleReclamation);
            showInfo("Réclamation ajoutée avec succès !");

            try {
                String destinataire = "roua.derouiche@esprit.tn"; // Fixed email address
                String sujet = "Nouvelle Réclamation Ajoutée";

                MailService.envoyerEmail(destinataire, sujet, titreFiltre, descriptionFiltre);
                System.out.println("✅ Email envoyé à : " + destinataire);
            } catch (Exception e) {
                System.err.println("❌ Erreur lors de l'envoi de l'email : " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            showError("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private Mission mission; // Mission passée à partir de la méthode d'ouverture

    // Méthode pour définir l'utilisateur
    public void setUtilisateur(User utilisateur) {
        if (utilisateur != null) {
            this.utilisateur = utilisateur;
        } else {
            System.out.println("⚠️ Utilisateur non défini. Utilisation d'un utilisateur par défaut.");
            this.utilisateur = new User(1, "Nom", "Prénom", "email@domain.com", "password", "freelance");
        }
    }


    // Méthode pour définir la mission
    public void setMission(Mission mission) {
        this.mission = mission;
    }
    private void closeWindow() {
        Stage stage = (Stage) titreTextField.getScene().getWindow();
        stage.close();
    }    @FXML
    public void annulerReclamation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Annulation");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir annuler l'ajout ?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                closeWindow();
            }
        });
    }}