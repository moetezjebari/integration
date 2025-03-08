package controllers.roua;

import controllers.AjoutReponseController;
import controllers.ModificationReponseController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Reclamation;
import models.Reponse;
import models.User;
import services.ReponseService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AffichageReponseController {

    @FXML
    private VBox reponsesContainer; // Conteneur pour afficher les réponses sous forme de cartes

    @FXML
    private Button btnAjouterReponse; // Bouton pour ajouter une réponse

    private Reclamation reclamation;
    private User utilisateur;
    private ReponseService reponseService;

    public void initialize() {
        reponseService = new ReponseService();
    }

    public void initialiser(Reclamation reclamation, User utilisateur) {
        this.reclamation = reclamation;
        this.utilisateur = utilisateur;
        chargerReponses();
    }

    private void chargerReponses() {
        try {
            List<Reponse> reponses = reponseService.recupererParReclamation(reclamation);
            reponsesContainer.getChildren().clear();

            if (reponses.isEmpty()) {
                Label labelVide = new Label("Aucune réponse disponible.");
                reponsesContainer.getChildren().add(labelVide);
                return;
            }

            for (Reponse reponse : reponses) {
                HBox card = new HBox(10);
                card.setStyle("-fx-border-color: #ddd; -fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-radius: 5; -fx-background-radius: 5;");

                // Image de la réponse
                ImageView imageView = new ImageView();
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);

                // Afficher l'image à partir des données de l'image
                if (reponse.getImageData() != null) {
                    Image image = new Image(new ByteArrayInputStream(reponse.getImageData()));
                    imageView.setImage(image);
                } else {
                    System.out.println("⚠️ Aucun tableau d'images enregistré.");
                }

                // Contenu et date de la réponse
                VBox content = new VBox(5);
                Label contenuLabel = new Label("Contenu: " + reponse.getContenu());
                Label dateLabel = new Label("Date: " + (reponse.getDateCreation() != null ? reponse.getDateCreation().toString() : "Non définie"));

                content.getChildren().addAll(contenuLabel, dateLabel);

                // Boutons Modifier et Supprimer
                Button btnModifier = new Button("Modifier");
                btnModifier.setOnAction(e -> afficherModificationReponse(reponse));

                Button btnSupprimer = new Button("Supprimer");
                btnSupprimer.setOnAction(e -> supprimerReponse(reponse));

                HBox buttonsBox = new HBox(5, btnModifier, btnSupprimer);

                card.getChildren().addAll(imageView, content, buttonsBox);
                reponsesContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du chargement des réponses : " + e.getMessage());
        }
    }

    @FXML
    private void ajouterReponse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutReponse.fxml"));
            Parent root = loader.load();

            AjoutReponseController ajoutReponseController = loader.getController();
            ajoutReponseController.setReclamation(reclamation);
            ajoutReponseController.setUser(utilisateur);

            Stage stage = new Stage();
            stage.setTitle("Ajouter Réponse");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            chargerReponses();
        } catch (IOException e) {
            System.out.println("❌ Erreur lors de l'ouverture de l'interface d'ajout de réponse : " + e.getMessage());
        }
    }

    private void afficherModificationReponse(Reponse reponse) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModificationReponse.fxml"));
            Parent root = loader.load();

            ModificationReponseController modificationController = loader.getController();
            modificationController.setReponse(reponse);

            Stage stage = new Stage();
            stage.setTitle("Modifier Réponse");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            chargerReponses();
        } catch (IOException e) {
            System.out.println("❌ Erreur lors de l'ouverture de l'interface de modification : " + e.getMessage());
        }
    }

    private void supprimerReponse(Reponse reponse) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette réponse ?");

        confirmation.showAndWait().ifPresent(reponseUtilisateur -> {
            if (reponseUtilisateur == ButtonType.OK) {
                try {
                    reponseService.supprimer(reponse.getId()); // Suppression de la réponse
                    afficherAlerte("Succès", "Réponse supprimée avec succès !");
                    chargerReponses(); // Recharger la liste après suppression
                } catch (SQLException e) {
                    afficherAlerte("Erreur", "Erreur lors de la suppression : " + e.getMessage());
                }
            }
        });
    }


    // Méthode pour définir la réclamation
    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        chargerReponses(); // Charger les réponses dès que la réclamation est définie
    }

    // Méthode pour définir l'utilisateur
    public void setUser(User utilisateur) {
        this.utilisateur = utilisateur;
    }
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
