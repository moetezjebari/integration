package controllers;

import models.Mission;
import models.PDFGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MissionDetailsController {
    @FXML
    private Label titreLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label competanceLabel;
    @FXML
    private Label dureeLabel;
    @FXML
    private Label budgetLabel;
    @FXML
    private Label datePubLabel;
    @FXML
    private Label questionsLabel;
    @FXML
    private Label nomEntrepriseLabel;

    private Mission mission;

    /**
     * Définit la mission à afficher et met à jour les labels.
     *
     * @param mission La mission à afficher.
     */
    public void setMission(Mission mission) {
        this.mission = mission;
        afficherDetails();
    }

    /**
     * Affiche les détails de la mission dans les labels.
     */
    private void afficherDetails() {
        titreLabel.setText(mission.getTitre());
        descriptionLabel.setText(mission.getDescription());
        competanceLabel.setText(mission.getCompetance());
        dureeLabel.setText(String.valueOf(mission.getDuree()));
        budgetLabel.setText(String.valueOf(mission.getBudget()));
        datePubLabel.setText(mission.getDatePub().toString());
        questionsLabel.setText(mission.getQuestions());
        nomEntrepriseLabel.setText(mission.getNomEntreprise());
    }

    /**
     * Ferme la fenêtre des détails de la mission.
     */
    @FXML
    public void retour() {
        Stage stage = (Stage) titreLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Génère un PDF avec les détails de la mission et l'ouvre automatiquement.
     */
    @FXML
    public void genererPDF() {
        if (mission != null) {
            String filePath = "mission_details.pdf"; // Chemin du fichier PDF
            PDFGenerator.generateMissionPDF(mission, filePath);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Le PDF a été généré avec succès : " + filePath);
            alert.showAndWait();

            // Ouvrir le fichier PDF automatiquement
            ouvrirFichierPDF(filePath);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Aucune mission sélectionnée.");
            alert.showAndWait();
        }
    }

    /**
     * Ouvre le fichier PDF avec l'application par défaut.
     *
     * @param filePath Le chemin du fichier PDF à ouvrir.
     */
    private void ouvrirFichierPDF(String filePath) {
        try {
            File pdfFile = new File(filePath);
            if (pdfFile.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                System.out.println("Impossible d'ouvrir le fichier PDF.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ouverture du fichier PDF : " + e.getMessage());
        }
    }
}