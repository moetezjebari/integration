package controllers;

import models.Mission;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.MissionService;

public class AjouterMissionController {
    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField competanceField;
    @FXML
    private TextField dureeField;
    @FXML
    private TextField budgetField;
    @FXML
    private DatePicker datePubPicker;
    @FXML
    private TextField questionsField;
    @FXML
    private TextField nomEntrepriseField; // Nouveau champ pour nomEntreprise

    private MissionService missionService = new MissionService();

    @FXML
    public void ajouterMission() {
        // Contrôle de saisie : vérifier que tous les champs sont remplis
        if (titreField.getText().isEmpty() || descriptionField.getText().isEmpty() || competanceField.getText().isEmpty() ||
                dureeField.getText().isEmpty() || budgetField.getText().isEmpty() || datePubPicker.getValue() == null ||
                questionsField.getText().isEmpty() || nomEntrepriseField.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Tous les champs doivent être remplis !");
            alert.showAndWait();
            return; // Arrêter l'exécution de la méthode
        }

        // Contrôle de saisie : vérifier que la durée et le budget sont des nombres valides
        try {
            int duree = Integer.parseInt(dureeField.getText());
            double budget = Double.parseDouble(budgetField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("La durée et le budget doivent être des nombres valides !");
            alert.showAndWait();
            return; // Arrêter l'exécution de la méthode
        }

        // Créer la mission
        Mission mission = new Mission(
                titreField.getText(),
                descriptionField.getText(),
                competanceField.getText(),
                Integer.parseInt(dureeField.getText()),
                Double.parseDouble(budgetField.getText()),
                datePubPicker.getValue(),
                questionsField.getText(),
                nomEntrepriseField.getText() // Ajout de nomEntreprise
        );

        // Ajouter la mission à la base de données
        missionService.add(mission);

        // Afficher un message de succès
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("La mission a été ajoutée avec succès !");
        alert.showAndWait();

        // Rediriger vers la liste des missions
        try {
            // Fermer la fenêtre actuelle
            Stage stage = (Stage) titreField.getScene().getWindow();
            stage.close();

            // Charger la vue AfficherListeMission.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/views/AfficherListeMission.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Liste des Missions");
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}