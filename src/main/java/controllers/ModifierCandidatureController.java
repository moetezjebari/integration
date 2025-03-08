package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.*;
import services.CandidatureService;
import services.MissionService;
import services.UtilisateurService;

import java.io.File;
import java.util.List;

public class ModifierCandidatureController {
    @FXML
    private TextField titreField;

    @FXML
    private ComboBox<Utilisateur> utilisateurComboBox;

    @FXML
    private ComboBox<Mission> missionComboBox;

    @FXML
    private Label nomImageLabel;

    @FXML
    private TextArea reponseQuestionsArea;

    private File imageSelectionnee; // Image sélectionnée par l'utilisateur
    private Candidature candidatureToUpdate;
    private CandidatureService candidatureService = new CandidatureService();
    private MissionService missionService = new MissionService();
    private UtilisateurService utilisateurService = new UtilisateurService();

    @FXML
    public void initialize() {
        // Charger les utilisateurs et missions dans les ComboBox
        chargerUtilisateurs();
        chargerMissions();
    }

    /**
     * Charge la liste des utilisateurs dans la ComboBox.
     */
    private void chargerUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.getAll();
        utilisateurComboBox.getItems().addAll(utilisateurs);
    }

    /**
     * Charge la liste des missions dans la ComboBox.
     */
    private void chargerMissions() {
        List<Mission> missions = missionService.getAll();
        missionComboBox.getItems().addAll(missions);
    }

    public void setCandidatureToUpdate(Candidature candidature) {
        this.candidatureToUpdate = candidature;
        utilisateurComboBox.setValue(candidature.getUtilisateur());
        missionComboBox.setValue(candidature.getMission());
        nomImageLabel.setText(candidature.getImage()); // Afficher le chemin de l'image existante
        reponseQuestionsArea.setText(candidature.getReponseQuestions());
    }

    @FXML
    public void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images PNG", "*.png")
        );
        imageSelectionnee = fileChooser.showOpenDialog(new Stage());

        if (imageSelectionnee != null) {
            nomImageLabel.setText(imageSelectionnee.getName());
        } else {
            nomImageLabel.setText("Aucune image sélectionnée");
        }
    }

    @FXML
    public void validerModification() {
        Utilisateur utilisateur = utilisateurComboBox.getValue();
        Mission mission = missionComboBox.getValue();
        String reponseQuestions = reponseQuestionsArea.getText();

        if (utilisateur == null || mission == null || imageSelectionnee == null || reponseQuestions.isEmpty()) {
            afficherAlerte("Erreur", "Champs manquants", "Veuillez remplir tous les champs et sélectionner une image.");
            return;
        }

        // Stocker le chemin de l'image sélectionnée
        String cheminImage = imageSelectionnee.getAbsolutePath();

        candidatureToUpdate.setUtilisateur(utilisateur);
        candidatureToUpdate.setMission(mission);
        candidatureToUpdate.setImage(cheminImage);
        candidatureToUpdate.setReponseQuestions(reponseQuestions);

        candidatureService.update(candidatureToUpdate);

        // Fermer la fenêtre après la modification
        Stage stage = (Stage) utilisateurComboBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void annulerModification() {
        Stage stage = (Stage) utilisateurComboBox.getScene().getWindow();
        stage.close();
    }

    /**
     * Affiche une alerte à l'utilisateur.
     */
    private void afficherAlerte(String titre, String entete, String contenu) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

}