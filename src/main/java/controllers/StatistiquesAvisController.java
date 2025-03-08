package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import models.StatistiqueAvis;
import services.StatistiqueAvisService;

import java.sql.SQLException;
import java.util.Map;

public class StatistiquesAvisController {

    @FXML
    private PieChart pieChartTypeAvis;

    private final StatistiqueAvisService statistiqueAvisService = new StatistiqueAvisService();

    @FXML
    public void initialize() throws SQLException {
        // Récupérer les statistiques
        StatistiqueAvis statistiqueAvis = statistiqueAvisService.obtenirStatistiques();

        // Afficher la répartition des types d'avis
        afficherRepartitionTypeAvis(statistiqueAvis);
    }

    private void afficherRepartitionTypeAvis(StatistiqueAvis statistiqueAvis) {
        // Calculer la répartition des types d'avis
        Map<String, Long> repartitionTypeAvis = statistiqueAvis.nombreAvisParType();

        // Ajouter les données au graphique en secteurs
        for (Map.Entry<String, Long> entry : repartitionTypeAvis.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey() + ": " + entry.getValue(), entry.getValue());
            pieChartTypeAvis.getData().add(slice);
        }
    }
}
