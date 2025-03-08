package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Candidature;
import models.QRCodeGenerator;
import services.CandidatureService;

import java.io.ByteArrayInputStream;
import java.util.List;

public class AfficherListeCandidatureController {
    @FXML
    private ListView<Candidature> candidatureListView;

    @FXML
    private ImageView qrCodeImageView; // Add this to your FXML file

    private CandidatureService candidatureService = new CandidatureService();

    @FXML
    public void initialize() {
        // Charger les candidatures dans la ListView
        chargerCandidatures();

        // Personnaliser l'affichage des candidatures
        candidatureListView.setCellFactory(param -> new ListCell<Candidature>() {
            @Override
            protected void updateItem(Candidature candidature, boolean empty) {
                super.updateItem(candidature, empty);

                if (empty || candidature == null) {
                    setText(null); // Afficher rien si la cellule est vide
                } else {
                    // Afficher les informations souhaitées (sans l'ID)
                    String texte = "Utilisateur: " + candidature.getUtilisateur().getNom() +
                            ", Mission: " + candidature.getMission().getTitre() +
                            ", Réponses: " + candidature.getReponseQuestions();
                    setText(texte);
                }
            }
        });
    }

    /**
     * Charge la liste des candidatures dans la ListView.
     */
    private void chargerCandidatures() {
        List<Candidature> candidatures = candidatureService.getAll();
        candidatureListView.getItems().clear(); // Vider la liste actuelle
        candidatureListView.getItems().addAll(candidatures); // Ajouter les candidatures
    }

    /**
     * Navigue vers l'interface d'ajout d'une candidature.
     */
    @FXML
    public void naviguerAjouterCandidature() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/AjouterCandidature.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigue vers l'interface de modification d'une candidature.
     */
    @FXML
    public void naviguerModifierCandidature() {
        Candidature candidatureSelectionnee = candidatureListView.getSelectionModel().getSelectedItem();
        if (candidatureSelectionnee != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierCandidature.fxml"));
                Parent root = loader.load();
                ModifierCandidatureController controller = loader.getController();
                controller.setCandidatureToUpdate(candidatureSelectionnee);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            afficherAlerte("Avertissement", "Aucune candidature sélectionnée", "Veuillez sélectionner une candidature à modifier.");
        }
    }

    /**
     * Supprime la candidature sélectionnée.
     */
    @FXML
    public void supprimerCandidature() {
        Candidature candidatureSelectionnee = candidatureListView.getSelectionModel().getSelectedItem();
        if (candidatureSelectionnee != null) {
            candidatureService.delete(candidatureSelectionnee.getId());
            chargerCandidatures(); // Rafraîchir la liste après la suppression
            afficherAlerte("Succès", "Candidature supprimée", "La candidature a été supprimée avec succès.");
        } else {
            afficherAlerte("Avertissement", "Aucune candidature sélectionnée", "Veuillez sélectionner une candidature à supprimer.");
        }
    }

    /**
     * Génère et affiche un QR code pour la candidature sélectionnée.
     */
    @FXML
    public void handleGenerateQRCodeButtonClick() {
        Candidature candidatureSelectionnee = candidatureListView.getSelectionModel().getSelectedItem();
        if (candidatureSelectionnee != null) {
            // Generate QR code content with additional details
            String qrContent = //"Candidature ID: " + candidatureSelectionnee.getId() + "\n"
                    "Utilisateur: " + candidatureSelectionnee.getUtilisateur().getNom() + "\n"
                            + "Mission: " + candidatureSelectionnee.getMission().getTitre() + "\n"
                            + "Réponses: " + candidatureSelectionnee.getReponseQuestions() + "\n"
                            + "Image: " + candidatureSelectionnee.getImage();

            try {
                // Generate QR code
                byte[] qrCodeImageBytes = QRCodeGenerator.generateQRCodeImage(qrContent, 200, 200);

                // Convert byte array to JavaFX Image
                Image qrCodeImage = new Image(new ByteArrayInputStream(qrCodeImageBytes));

                // Display QR code in ImageView
                qrCodeImageView.setImage(qrCodeImage);
            } catch (Exception e) {
                e.printStackTrace();
                afficherAlerte("Erreur", "Génération du QR code", "Impossible de générer le QR code.");
            }
        } else {
            afficherAlerte("Avertissement", "Aucune candidature sélectionnée", "Veuillez sélectionner une candidature pour générer un QR code.");
        }
    }

    /**
     * Affiche une alerte à l'utilisateur.
     *
     * @param titre   Le titre de l'alerte.
     * @param entete  L'en-tête de l'alerte.
     * @param contenu Le contenu de l'alerte.
     */
    private void afficherAlerte(String titre, String entete, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}