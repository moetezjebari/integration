package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import controllers.AfficherReclamationController;
import controllers.ModifierReclamationController;
import controllers.AjouterReclamationControllers; // Importez le contrôleur pour l'ajout de réclamation
import models.Reclamation;
import models.User;
import models.Mission;
import java.io.IOException;
import java.util.Date;

public class MainFX extends Application {

    private User utilisateur; // Utilisateur courant
    private Mission mission; // Mission courante

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReclamation.fxml"));
            AnchorPane root = loader.load();

            // Initialiser le contrôleur de l'affichage des réclamations
            AfficherReclamationController controller = loader.getController();

            // Créez un utilisateur avec l'ID 1 (modifiez les attributs selon vos besoins)
            utilisateur = new User(1, "Nom", "Prénom", "email@example.com", "motDePasse", "freelance");
            controller.setUser(utilisateur); // Passer l'utilisateur au contrôleur

            // Créez une mission (modifiez selon votre constructeur)
            mission = new Mission(7, "Titre de la mission", "Description de la mission", 1000, new Date());
            controller.setMission(mission); // Passer la mission au contrôleur

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gestion des Réclamations");

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ouvrir la fenêtre d'ajout de réclamation
    public static void openAjouterReclamationWindow(Stage owner, User utilisateur, Mission mission) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/AjouterReclamation.fxml"));
            AnchorPane root = loader.load();

            // Récupérer le contrôleur d'ajout de réclamation
            AjouterReclamationControllers controller = loader.getController();
            controller.setUtilisateur(utilisateur); // Définir l'utilisateur
            controller.setMission(mission); // Définir la mission

            Stage ajouterStage = new Stage();
            ajouterStage.setTitle("Ajouter Réclamation");
            ajouterStage.setScene(new Scene(root));
            ajouterStage.initModality(Modality.APPLICATION_MODAL);
            ajouterStage.initOwner(owner);
            ajouterStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Ouvrir la fenêtre de modification de réclamation
    public static void openModifierReclamationWindow(Stage owner, Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/ModifierReclamation.fxml"));
            AnchorPane root = loader.load();

            ModifierReclamationController controller = loader.getController();
            controller.initData(reclamation); // Passer la réclamation au contrôleur

            Stage modifierStage = new Stage();
            modifierStage.setTitle("Modifier Réclamation");
            modifierStage.setScene(new Scene(root));
            modifierStage.initModality(Modality.APPLICATION_MODAL);
            modifierStage.initOwner(owner);
            modifierStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
