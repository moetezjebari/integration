package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Avis;
import models.User;
import models.Utilisateur;
import services.AvisService;
import utils.BadWordFilter;

import java.time.LocalDateTime;

public class AjouterAvisController {

    @FXML
    private TextArea txtCommentaire;

    @FXML
    private HBox starContainer; // Conteneur pour les étoiles

    @FXML
    private CheckBox chkservice, chkformation, chkautre; // Cases à cocher pour le type d'avis
    private User user; // ✅ Add a User variable

    public void setUser(User user) { // ✅ Add this method
        this.user = user;
        System.out.println("User set in AjouterAvisController: " + user.getNom());
    }
    private final AvisService avisService = new AvisService();
    private Utilisateur utilisateur; // Changed User to Utilisateur
    private AvisController avisController;

    private int noteSelectionnee = 0; // Note sélectionnée par l'utilisateur

    private static final String ERREUR_CHAMPS_VIDES = "Tous les champs sont requis !";
    private static final String ERREUR_NOTE_INVALIDE = "Veuillez sélectionner une note avec les étoiles !";
    private static final String ERREUR_UTILISATEUR_NON_DEFINI = "Aucun utilisateur défini !";
    private static final String ERREUR_TYPE_AVIS = "Veuillez sélectionner un type d'avis !";
    private static final String SUCCES_AJOUT = "Avis ajouté avec succès !";

    public void setUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) {
            utilisateur = new Utilisateur();
            utilisateur.setId(1);
        }
        this.utilisateur = utilisateur;
    }




    public void setAvisController(AvisController avisController) {
        this.avisController = avisController;
    }

    @FXML
    public void initialize() {
        creerEtoiles();
    }

    private void creerEtoiles() {
        starContainer.getChildren().clear();
        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView(new Image(getClass().getResourceAsStream("/images/star_empty.png")));
            star.setFitWidth(32);
            star.setFitHeight(32);
            final int etoileIndex = i;
            star.setOnMouseClicked(event -> setNote(etoileIndex));
            starContainer.getChildren().add(star);
        }
    }

    private void setNote(int note) {
        this.noteSelectionnee = note;
        mettreAJourEtoiles();
    }

    private void mettreAJourEtoiles() {
        for (int i = 0; i < starContainer.getChildren().size(); i++) {
            ImageView star = (ImageView) starContainer.getChildren().get(i);
            if (i < noteSelectionnee) {
                star.setImage(new Image(getClass().getResourceAsStream("/images/star_filled.png")));
            } else {
                star.setImage(new Image(getClass().getResourceAsStream("/images/star_empty.png")));
            }
        }
    }

    @FXML
    public void ajouterAvis(ActionEvent event) {
        try {
            String typeAvis = getTypeAvisSelectionne();
            if (typeAvis == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", ERREUR_TYPE_AVIS);
                return;
            }

            if (txtCommentaire.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", ERREUR_CHAMPS_VIDES);
                return;
            }

            if (noteSelectionnee < 1 || noteSelectionnee > 5) {
                showAlert(Alert.AlertType.ERROR, "Erreur", ERREUR_NOTE_INVALIDE);
                return;
            }

            if (utilisateur == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", ERREUR_UTILISATEUR_NON_DEFINI);
                return;
            }

            String commentaireFiltre = BadWordFilter.filterBadWords(txtCommentaire.getText().trim());

            Avis avis = new Avis();
            avis.setUtilisateur(utilisateur); // Updated to setUtilisateur
            avis.setTypeAvis(typeAvis);
            avis.setCommentaire(commentaireFiltre);
            avis.setNote(noteSelectionnee);
            avis.setDateAvis(LocalDateTime.now());

            avisService.ajouter(avis);
            showAlert(Alert.AlertType.INFORMATION, "Succès", SUCCES_AJOUT);

            if (avisController != null) {
                avisController.chargerAvis("Tous"); // Appel de la méthode publique
            }

            fermerFenetre(event);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue !");
            e.printStackTrace();
        }
    }

    private String getTypeAvisSelectionne() {
        if (chkservice.isSelected()) return "service";
        if (chkformation.isSelected()) return "formation";
        if (chkautre.isSelected()) return "autre";
        return null;
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void fermerFenetre(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
