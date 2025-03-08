package tests;

/*import models.Reclamation;
import services.ReclamationService;

import java.sql.SQLException;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        ReclamationService rs = new ReclamationService();

        try {
            // 1. Ajouter une réclamation
            System.out.println("Ajout d'une nouvelle réclamation...");
            Reclamation nouvelleReclamation = new Reclamation(1, "Problème de paiement", 6, "en_attente", new Date(), "Délai de paiement non respecté");

            rs.ajouter(nouvelleReclamation);
            System.out.println("Réclamation ajoutée avec succès !");
            // 2. Modifier une réclamation
            System.out.println("\nModification d'une réclamation...");
            // Assurez-vous que l'ID existe dans la base de données
            Reclamation reclamationExistante = rs.recuperer().stream()
                    .filter(r -> r.getId() == 4) // Cibler la réclamation avec l'ID 2
                    .findFirst()
                    .orElse(null);

            if (reclamationExistante != null) {
                // Modifier la description de la réclamation
                String nouvelleDescription = "Délai de paiement largement dépassé";
                rs.modifier(reclamationExistante, nouvelleDescription);
                System.out.println("Réclamation modifiée avec succès !");
            } else {
                System.out.println("Réclamation avec l'ID 1 non trouvée.");
            }
            // . Supprimer une réclamation
            System.out.println("\nSuppression de la réclamation...");
            // Cibler la réclamation avec un ID connu
            Reclamation reclamationASupprimer = rs.recuperer().stream()
                    .filter(r -> r.getId() == 5)  // Cibler la réclamation avec l'ID 2
                    .findFirst()
                    .orElse(null);

            if (reclamationASupprimer != null) {
                // Supprimer la réclamation
                rs.supprimer(reclamationASupprimer);
                System.out.println("Réclamation supprimée avec succès !");
            } else {
                System.out.println("Réclamation avec l'ID 2 non trouvée.");
            }

            // 3. Afficher les réclamations après suppression
            System.out.println("\nListe des réclamations après suppression :");
            rs.recuperer().forEach(System.out::println);


        } catch (SQLException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}*/



import models.Reponse;
import services.ReponseService;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        ReponseService service = new ReponseService();

        // Sélectionner une image de test
        File imageTest = new File("test_images/roua.gif"); // Mets une image ici !

        if (!imageTest.exists()) {
            System.out.println("L'image test n'existe pas. Ajoute un fichier dans test_images/ !");
            return;
        }

        // Uploader l'image
        String imagePath = service.uploaderImage(imageTest);

        if (imagePath == null) {
            System.out.println("L'upload a échoué.");
            return;
        }

        // Ajouter une réponse avec l'image
        Reponse reponse = new Reponse(6, 1, "Ceci est une réponse avec image", imagePath, new Date());

        try {
            service.ajouter(reponse);
            System.out.println("✅ Réponse enregistrée avec succès !");

           // 2. Modifier une réponse
            System.out.println("\nModification d'une réponse...");
            // Assurez-vous que l'ID existe dans la base de données
            Reponse reponseExistante = service.recuperer().stream()
                    .filter(r -> r.getId() == 7) // Cibler la réponse avec l'ID 4 (à ajuster)
                    .findFirst()
                    .orElse(null);

            if (reponseExistante != null) {
                // Modifier le contenu de la réponse et le chemin de l'image
                reponseExistante.setContenu("rourouououououou.");
                reponseExistante.setImagePath("nouveau_chemin_image.jpg"); // Assurez-vous d'avoir un chemin valide si vous changez l'image
                service.modifier(reponseExistante);
                System.out.println("Réponse modifiée avec succès !");
            } else {
                System.out.println("Réponse avec l'ID 4 non trouvée.");
            }

            // 3. Supprimer une réponse
            System.out.println("\nSuppression de la réponse...");
            // Cibler la réponse avec un ID connu
            Reponse reponseASupprimer = service.recuperer().stream()
                    .filter(r -> r.getId() == 5)  // Cibler la réponse avec l'ID 5 (à ajuster)
                    .findFirst()
                    .orElse(null);

            if (reponseASupprimer != null) {
                // Supprimer la réponse
                service.supprimer(reponseASupprimer);
                System.out.println("Réponse supprimée avec succès !");
            } else {
                System.out.println("Réponse avec l'ID 5 non trouvée.");
            }

            // 4. Afficher les réponses après suppression
            System.out.println("\nListe des réponses après suppression :");
            service.recuperer().forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}



