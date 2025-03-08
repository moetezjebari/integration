package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Avis;
import models.User;
import models.Utilisateur; // Changed to Utilisateur
import services.AvisService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AvisController {

    @FXML
    private GridPane avisGridPane;

    @FXML
    private Button ajouterAvisButton;

    @FXML
    private Button ouvrirChatButton; // Ajout du bouton pour ouvrir le chat
    @FXML
    private Button ouvrirStatistiquesButton;
    @FXML
    private javafx.scene.control.ComboBox<String> filtreTypeComboBox;


    private final AvisService avisService = new AvisService();
    private Utilisateur utilisateur;
    private User user;
    // Changed User to Utilisateur
    public void setUser(User user) { // ✅ Add this method
        this.user = user;
        System.out.println("User set in AvisController: " + user.getNom());
    }
    @FXML
    public void initialize() {
        // Remplir la ComboBox avec les types d'avis possibles
        filtreTypeComboBox.setItems(FXCollections.observableArrayList("Tous", "service", "formation", "autre"));
        filtreTypeComboBox.setValue("Tous"); // Valeur par défaut

        // Ajouter un écouteur pour détecter le changement de filtre
        filtreTypeComboBox.setOnAction(event -> {
            String typeAvis = filtreTypeComboBox.getValue(); // Récupérer le type sélectionné
            chargerAvis(typeAvis); // Charger les avis en fonction du type
        });

        // Charger tous les avis au démarrage
        chargerAvis("Tous");
    }

    private void filtrerAvis() {
        String typeAvis = filtreTypeComboBox.getValue(); // Récupérer le type sélectionné
        chargerAvis(typeAvis); // Passer le type d'avis à chargerAvis
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        if (this.utilisateur == null) {
            this.utilisateur = new Utilisateur();
            this.utilisateur.setId(1); // Setting default ID to 1
        }
    }


    // Dans AvisController.java
    public void chargerAvis(String typeAvis) {
        try {
            avisGridPane.getChildren().clear(); // Effacer les avis actuels
            List<Avis> avisList;

            // Récupérer les avis en fonction du type sélectionné
            if ("Tous".equals(typeAvis)) {
                avisList = avisService.recuperer(); // Tous les avis
            } else {
                avisList = avisService.filtrerAvisParType(typeAvis); // Avis filtrés
            }

            int row = 0;
            int col = 0;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            // Afficher les avis dans le GridPane
            for (Avis avis : avisList) {
                VBox card = new VBox();
                card.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 1, 1);");

                Label typeLabel = new Label("Type : " + avis.getTypeAvis());
                Label commentaireLabel = new Label("Commentaire : " + avis.getCommentaire());
                Label noteLabel = new Label("Note : " + avis.getNote() + "/5");

                // Conversion de LocalDateTime à Date pour le formatage
                Date dateAvis = Date.from(avis.getDateAvis().atZone(ZoneId.systemDefault()).toInstant());
                Label dateLabel = new Label("Date : " + dateFormat.format(dateAvis));

                Button modifierButton = new Button("Modifier");
                Button supprimerButton = new Button("Supprimer");

                modifierButton.setOnAction(e -> modifierAvis(avis));
                supprimerButton.setOnAction(e -> supprimerAvis(avis));

                card.getChildren().addAll(typeLabel, commentaireLabel, noteLabel, dateLabel, modifierButton, supprimerButton);
                avisGridPane.add(card, col, row);

                col++;
                if (col > 2) {
                    col = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des avis : " + e.getMessage());
        }
    }

    @FXML
    private void ajouterAvis() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAvis.fxml"));
            Scene scene = new Scene(loader.load());
            AjouterAvisController controller = loader.getController();

            if (utilisateur == null) {
                utilisateur = new Utilisateur();
                utilisateur.setId(1);
            }

            controller.setUtilisateur(utilisateur);  // Pass the utilisateur object
            controller.setAvisController(this);  // Link to the parent controller

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'avis : " + e.getMessage());
        }
    }


    private void modifierAvis(Avis avis) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAvis.fxml"));
            Scene scene = new Scene(loader.load());
            ModifierAvisController controller = loader.getController();
            controller.setAvis(avis);
            controller.setAvisController(this); // Passer le contrôleur

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            // Fermer la fenêtre une fois que l'avis est modifié
            stage.setOnCloseRequest(e -> {
                String typeAvis = filtreTypeComboBox.getValue(); // Récupérer le type sélectionné
                chargerAvis(typeAvis); // Passer le type d'avis à chargerAvis
            });
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification de l'avis : " + e.getMessage());
        }
    }

    private void supprimerAvis(Avis avis) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet avis ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    avisService.supprimer(avis);
                    String typeAvis = filtreTypeComboBox.getValue(); // Récupérer le type sélectionné
                    chargerAvis(typeAvis); // Passer le type d'avis à chargerAvis
                    afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Avis supprimé avec succès !");
                } catch (SQLException e) {
                    afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de l'avis : " + e.getMessage());
                }
            }
        });
    }

    // Méthode pour ouvrir le chat
    @FXML
    private void ouvrirChat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatInterface.fxml")); // Remplacez par le bon chemin
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Chat avec le Chatbot");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture du chat : " + e.getMessage());
        }
    }

    @FXML
    private void ouvrirStatistiques() {
        try {
            // Charger la fenêtre de statistiques (assurez-vous que le fichier FXML pour les statistiques existe)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatistiqueAvis.fxml"));
            Scene scene = new Scene(loader.load());

            // Créez une nouvelle fenêtre pour afficher les statistiques
            Stage stage = new Stage();
            stage.setTitle("Statistiques des Avis");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture des statistiques : " + e.getMessage());
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
