package main;

import controllers.*;
import controllers.roua.AffichageReponseController;
import controllers.roua.AfficherReclamationController;
import controllers.roua.AjouterReclamationControllers;
import controllers.roua.ModifierReclamationController;
import org.example.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.util.Date;


public class MainFX extends Application {

    private static Stage primaryStage; // Référence statique au stage principal
    private User utilisateur; // Utilisateur courant
    private Mission mission; // Mission courante

    @Override
    public void start(Stage primaryStage) {
        MainFX.primaryStage = primaryStage; // Initialisation de la référence statique
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml")); // Remplacez par un autre fichier FXML si nécessaire
            Parent root = loader.load();

            // Création d'un utilisateur fictif
            utilisateur = new User(4, "Nom", "Prénom", "roua.derouiche@esprit.tn", "motDePasse", "freelance");

            // Création d'une mission fictive
            mission = new Mission(7, "Titre de la mission", "Description de la mission", 1000, new Date());

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Freelance Connect - Accueil");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ==================== GESTION DES RECLAMATIONS ====================

    public static void openAfficherReclamationWindow(Stage owner, User utilisateur, Mission mission) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/AfficherReclamation.fxml"));
            AnchorPane root = loader.load();

            AfficherReclamationController controller = loader.getController();
            controller.setUser(utilisateur);
            controller.setMission(mission);

            Stage stage = new Stage();
            stage.setTitle("Liste des Réclamations");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openAjouterReclamationWindow(Stage owner, User utilisateur, Mission mission) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/AjouterReclamation.fxml"));
            AnchorPane root = loader.load();

            AjouterReclamationControllers controller = loader.getController();
            controller.setUtilisateur(utilisateur);
            controller.setMission(mission);

            Stage stage = new Stage();
            stage.setTitle("Ajouter Réclamation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openModifierReclamationWindow(Stage owner, Reclamation reclamation, User utilisateur, Mission mission) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/ModifierReclamation.fxml"));
            AnchorPane root = loader.load();

            ModifierReclamationController controller = loader.getController();
            controller.initData(reclamation);
            controller.setUtilisateur(utilisateur);
            controller.setMission(mission);

            Stage stage = new Stage();
            stage.setTitle("Modifier Réclamation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ==================== GESTION DES REPONSES ====================

    public static void openAjouterReponseWindow(Stage owner, Reclamation reclamation, User utilisateur) {
        if (utilisateur == null) {
            new Alert(Alert.AlertType.ERROR, "L'utilisateur est null, impossible d'ouvrir la fenêtre d'ajout de réponse.").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/AjoutReponse.fxml"));
            AnchorPane root = loader.load();

            AjoutReponseController controller = loader.getController();
            controller.setReclamation(reclamation);
            controller.setUser(utilisateur);

            Stage stage = new Stage();
            stage.setTitle("Ajouter Réponse");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openModifierReponseWindow(Stage owner, Reponse reponse) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/ModificationReponse.fxml"));
            AnchorPane root = loader.load();

            ModificationReponseController controller = loader.getController();
            controller.setReponse(reponse);

            Stage stage = new Stage();
            stage.setTitle("Modifier Réponse");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openReponsesWindow(Stage owner, Reclamation reclamation, User utilisateur) {
        if (utilisateur == null) {
            new Alert(Alert.AlertType.ERROR, "L'utilisateur est null, impossible d'ouvrir la fenêtre des réponses.").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/AffichageReponse.fxml"));
            AnchorPane root = loader.load();

            AffichageReponseController controller = loader.getController();
            controller.initialiser(reclamation, utilisateur);

            Stage stage = new Stage();
            stage.setTitle("Réponses à la Réclamation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("❌ Erreur lors du chargement du fichier FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== GESTION DES AVIS ====================

    public static void openAfficherAvisWindow(Stage owner, User utilisateur) {
        if (utilisateur == null) {
            new Alert(Alert.AlertType.ERROR, "L'utilisateur est null, impossible d'ouvrir la fenêtre des avis.").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/AvisView.fxml"));
            AnchorPane root = loader.load();

            AvisController controller = loader.getController();
            controller.setUser(utilisateur);

            Stage stage = new Stage();
            stage.setTitle("Liste des Avis");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openAjouterAvisWindow(Stage owner, User utilisateur) {
        if (utilisateur == null) {
            new Alert(Alert.AlertType.ERROR, "L'utilisateur est null, impossible d'ouvrir la fenêtre d'ajout d'avis.").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/AjouterAvis.fxml"));
            AnchorPane root = loader.load();

            AjouterAvisController controller = loader.getController();
            controller.setUser(utilisateur);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Avis");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openModifierAvisWindow(Stage owner, Avis avis) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/ModifierAvis.fxml"));
            AnchorPane root = loader.load();

            ModifierAvisController controller = loader.getController();
            controller.setAvis(avis);

            Stage stage = new Stage();
            stage.setTitle("Modifier un Avis");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage; // Méthode pour obtenir le stage principal
    }

    public static void main(String[] args) {
        launch(args);
    }
}
