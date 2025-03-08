package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Avis;
import services.AvisService;
import utils.BadWordFilter;

import java.sql.SQLException;
import java.util.Objects;

public class ModifierAvisController {

    @FXML
    private TextArea commentaireField;

    @FXML
    private HBox starContainer; // Conteneur des étoiles

    @FXML
    private CheckBox chkservice, chkformation, chkautre; // Cases à cocher pour le type d'avis

    private final AvisService avisService = new AvisService();
    private Avis avis;
    private AvisController avisController; // Référence pour rafraîchir la liste après modification

    private int noteSelectionnee = 0; // Note actuelle sélectionnée

    private static final String ERREUR_CHAMPS_VIDES = "Tous les champs sont requis !";
    private static final String ERREUR_NOTE_INVALIDE = "Veuillez sélectionner une note avec les étoiles !";
    private static final String ERREUR_TYPE_AVIS = "Veuillez sélectionner un type d'avis !";
    private static final String SUCCES_MODIFICATION = "Avis modifié avec succès !";
    private static final String ERREUR_MODIFICATION = "Une erreur est survenue lors de la modification !";

    // Associe un avis existant aux champs du formulaire
    public void setAvis(Avis avis) {
        this.avis = avis;
        if (avis != null) {
            commentaireField.setText(avis.getCommentaire());
            noteSelectionnee = avis.getNote();
            mettreAJourEtoiles();

            // Sélectionne le bon type d'avis
            switch (avis.getTypeAvis()) {
                case "service" -> chkservice.setSelected(true);
                case "formation" -> chkformation.setSelected(true);
                case "autre" -> chkautre.setSelected(true);
            }
        }
    }

    // Initialise les étoiles au chargement
    @FXML
    public void initialize() {
        creerEtoiles();
    }

    // Crée les étoiles interactives
    private void creerEtoiles() {
        starContainer.getChildren().clear();
        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/star_empty.png"))));
            star.setFitWidth(32);
            star.setFitHeight(32);
            final int etoileIndex = i;
            star.setOnMouseClicked(event -> setNote(etoileIndex));
            starContainer.getChildren().add(star);
        }
    }

    // Met à jour la note en fonction des étoiles cliquées
    private void setNote(int note) {
        this.noteSelectionnee = note;
        mettreAJourEtoiles();
    }

    // Change les étoiles en fonction de la note sélectionnée
    private void mettreAJourEtoiles() {
        for (int i = 0; i < starContainer.getChildren().size(); i++) {
            ImageView star = (ImageView) starContainer.getChildren().get(i);
            if (i < noteSelectionnee) {
                star.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/star_filled.png"))));
            } else {
                star.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/star_empty.png"))));
            }
        }
    }

    // Permet d'obtenir le type d'avis sélectionné
    private String getTypeAvisSelectionne() {
        if (chkservice.isSelected()) return "service";
        if (chkformation.isSelected()) return "formation";
        if (chkautre.isSelected()) return "autre";
        return null;
    }

    // Permet de récupérer une référence du contrôleur principal
    public void setAvisController(AvisController avisController) {
        this.avisController = avisController;
    }

    @FXML
    private void modifierAvis() {
        try {
            String typeAvis = getTypeAvisSelectionne();
            if (typeAvis == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", ERREUR_TYPE_AVIS);
                return;
            }

            if (commentaireField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", ERREUR_CHAMPS_VIDES);
                return;
            }

            if (noteSelectionnee < 1 || noteSelectionnee > 5) {
                showAlert(Alert.AlertType.ERROR, "Erreur", ERREUR_NOTE_INVALIDE);
                return;
            }

            // 🔥 Filtrage des bad words
            String commentaireFiltre = BadWordFilter.filterBadWords(commentaireField.getText().trim());

            // Mise à jour de l'avis
            avis.setTypeAvis(typeAvis);
            avis.setCommentaire(commentaireFiltre);
            avis.setNote(noteSelectionnee);

            avisService.modifier(avis);
            showAlert(Alert.AlertType.INFORMATION, "Succès", SUCCES_MODIFICATION);

            if (avisController != null) {
                avisController.chargerAvis("Tous"); // Appel de la méthode publique
            }


            fermerFenetre();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", ERREUR_MODIFICATION);
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur inattendue est survenue !");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) commentaireField.getScene().getWindow();
        stage.close();
    }
}
