package tests;

import models.Reclamation;
import models.User;
import models.Mission;
import services.ReclamationService;
import tools.MyDataBase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MainReclamation {
    public static void main(String[] args) {
        // Connexion à la base de données
        Connection cnx = MyDataBase.getInstance().getCnx();
        if (cnx == null) {
            System.out.println("Erreur de connexion à la base de données.");
            return;
        }

        ReclamationService reclamationService = new ReclamationService();

        try {
            // Création d'un objet User et Mission pour la réclamation
            // Vérifiez que l'ID de l'utilisateur et de la mission existent dans la base de données
            User user = new User(1, "Dupont", "Jean", "jean.dupont@example.com", "password123", "freelance");
            Mission mission = new Mission(6, "Développement d'application", "Développement d'une application mobile", 1500, java.sql.Date.valueOf("2025-03-01"));

            // Vérification des données avant l'ajout
            String status = "en_attente"; // Vérifiez que ce statut est valide pour la base
            if (status.length() > 50) { // Ajustez selon la taille de la colonne
                throw new IllegalArgumentException("Le statut est trop long !");
            }

            // Création d'une nouvelle réclamation
            Reclamation newReclamation = new Reclamation(0, user, "Réclamation concernant le retard", mission, status, java.sql.Date.valueOf("2025-02-17"), "Problème de délai");

            // Ajout de la réclamation
            try {
                reclamationService.ajouter(newReclamation);
                System.out.println("✅ Réclamation ajoutée avec succès !");
            } catch (SQLException e) {
                System.out.println("❌ Erreur lors de l'ajout de la réclamation : " + e.getMessage());
            }

            // Affichage des réclamations
            System.out.println("\n📜 Liste des réclamations :");
            List<Reclamation> reclamations = reclamationService.recuperer();
            if (reclamations.isEmpty()) {
                System.out.println("❌ Aucune réclamation trouvée.");
            } else {
                reclamations.forEach(r -> System.out.println(r.getId() + " : " + r.getTitre() + " - Status: " + r.getStatus()));
            }

            // Suppression d'une réclamation (vérification de l'ID avant)
            int idReclamationASupprimer = 14;
            Reclamation reclamationASupprimer = reclamations.stream()
                    .filter(r -> r.getId() == idReclamationASupprimer)
                    .findFirst()
                    .orElse(null);

            if (reclamationASupprimer != null) {
                try {
                    reclamationService.supprimer(reclamationASupprimer);
                    System.out.println("\n✅ Réclamation supprimée avec succès !");
                } catch (SQLException e) {
                    System.out.println("❌ Erreur lors de la suppression de la réclamation : " + e.getMessage());
                }
            } else {
                System.out.println("❌ Réclamation avec l'ID " + idReclamationASupprimer + " non trouvée.");
            }

            // Modification d'une réclamation (vérification de l'ID avant)
            int idReclamationAModifier = 13;
            Reclamation reclamationAModifier = reclamations.stream()
                    .filter(r -> r.getId() == idReclamationAModifier)
                    .findFirst()
                    .orElse(null);

            if (reclamationAModifier != null) {
                reclamationAModifier.setTitre("Nouvelle réclamation");
                try {
                    reclamationService.modifier(reclamationAModifier);
                    System.out.println("\n✅ Réclamation modifiée avec succès !");
                } catch (SQLException e) {
                    System.out.println("❌ Erreur lors de la modification de la réclamation : " + e.getMessage());
                }
            } else {
                System.out.println("❌ Réclamation avec l'ID " + idReclamationAModifier + " non trouvée.");
            }

            // Affichage des réclamations après modification
            System.out.println("\n📜 Liste des réclamations après modification :");
            reclamationService.recuperer().forEach(r -> System.out.println(r.getId() + " : " + r.getTitre() + " - Status: " + r.getStatus()));

        } catch (SQLException e) {
            System.out.println("❌ Erreur générale lors du CRUD des réclamations : " + e.getMessage());
        }
    }
}
