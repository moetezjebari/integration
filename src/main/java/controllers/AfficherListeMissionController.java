package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.*;
import services.CandidatureService;
import services.MissionService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherListeMissionController {
    @FXML
    private ListView<Mission> missionListView;

    @FXML
    private TextField rechercheTextField;

    private MissionService missionService = new MissionService();
    private CandidatureService candidatureService = new CandidatureService(); // Ajout

    @FXML
    public void initialize() {
        chargerMissions();

        // Gérer la sélection d'une mission
        missionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ouvrirDetailsMission(newSelection);
            }
        });
    }

    private void chargerMissions() {
        List<Mission> missions = missionService.getAll();

        // Ajouter le nombre de candidatures pour chaque mission
        missions.forEach(mission -> {
            int nombreCandidatures = candidatureService.compterCandidaturesParMission(mission.getId());
            mission.setNombreCandidatures(nombreCandidatures); // Supposons que vous avez ajouté ce champ dans la classe Mission
        });

        missionListView.getItems().clear();
        missionListView.getItems().addAll(missions);

        // Personnaliser l'affichage des missions dans la ListView
        missionListView.setCellFactory(param -> new MissionListCell());
    }

    @FXML
    public void rechercherParTitre() {
        String titreRecherche = rechercheTextField.getText();
        List<Mission> missions = missionService.getAll();

        List<Mission> missionsFiltrees = missions.stream()
                .filter(mission -> mission.getTitre().toLowerCase().contains(titreRecherche.toLowerCase()))
                .collect(Collectors.toList());

        missionListView.getItems().clear();
        missionListView.getItems().addAll(missionsFiltrees);
    }

    @FXML
    public void trierParTitre() {
        List<Mission> missions = missionService.getAll();

        List<Mission> missionsTriees = missions.stream()
                .sorted(Comparator.comparing(Mission::getTitre))
                .collect(Collectors.toList());

        missionListView.getItems().clear();
        missionListView.getItems().addAll(missionsTriees);
    }

    private void ouvrirDetailsMission(Mission mission) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MissionDetails.fxml"));
            Parent root = loader.load();

            MissionDetailsController controller = loader.getController();
            controller.setMission(mission);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de la Mission");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void naviguerAjouterMission() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/AjouterMission.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void naviguerModifierMission() {
        Mission missionSelectionnee = missionListView.getSelectionModel().getSelectedItem();
        if (missionSelectionnee != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierMission.fxml"));
                Parent root = loader.load();
                ModifierMissionController controller = loader.getController();
                controller.setMissionToUpdate(missionSelectionnee);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une mission à modifier.");
            alert.showAndWait();
        }
    }

    @FXML
    public void supprimerMission() {
        Mission missionSelectionnee = missionListView.getSelectionModel().getSelectedItem();
        if (missionSelectionnee != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette mission ?");

            if (alert.showAndWait().get().getText().equals("OK")) {
                missionService.delete(missionSelectionnee.getId());
                missionListView.getItems().remove(missionSelectionnee);
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une mission à supprimer.");
            alert.showAndWait();
        }
    }
}