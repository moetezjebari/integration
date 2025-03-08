package services;

import models.Avis;
import models.StatistiqueAvis;

import java.sql.SQLException;
import java.util.List;

public class StatistiqueAvisService {

    private final AvisService avisService;

    public StatistiqueAvisService() {
        this.avisService = new AvisService(); // Service pour récupérer les avis
    }

    // Méthode pour obtenir les statistiques des avis
    public StatistiqueAvis obtenirStatistiques() throws SQLException {
        List<Avis> avisList = avisService.listerTousAvis(); // Récupérer les avis depuis la source de données
        return new StatistiqueAvis(avisList); // Retourne un objet StatistiqueAvis contenant les calculs
    }
}
