package controllers;

import models.Mission;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

public class MissionListCell extends ListCell<Mission> {
    @Override
    protected void updateItem(Mission mission, boolean empty) {
        super.updateItem(mission, empty);

        if (empty || mission == null) {
            setText(null);
            setGraphic(null);
        } else {
            VBox vbox = new VBox();
            Label titreLabel = new Label(mission.getTitre());
            Label entrepriseLabel = new Label("Entreprise : " + mission.getNomEntreprise());
            Label candidaturesLabel = new Label("Candidatures : " + mission.getNombreCandidatures());

            titreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            entrepriseLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
            candidaturesLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

            vbox.getChildren().addAll(titreLabel, entrepriseLabel, candidaturesLabel);
            setGraphic(vbox);
        }
    }
}