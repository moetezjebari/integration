package controllers.roua;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.MainFX;
import models.Mission;
import models.Reclamation;
import models.User;
import services.ReclamationService;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherReclamationController {

    @FXML
    private GridPane reclamationGridPane;

    @FXML
    private Button ajouterButton;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button sortButton;




    private ReclamationService reclamationService = new ReclamationService();
    private User utilisateur;
    private Mission mission;
    private boolean isSortedAsc = true; // Variable pour basculer entre tri ascendant et descendant

    @FXML
    public void initialize() {
        chargerReclamations();
        searchButton.setOnAction(e -> rechercherReclamation());
        sortButton.setOnAction(e -> trierParNombreDeReponses());


    }

    public void setUser(User utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    private void chargerReclamations() {
        try {
            afficherReclamations(reclamationService.recuperer());
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des réclamations : " + e.getMessage());
        }
    }

    public void rechercherReclamation() {
        String titreRecherche = searchField.getText().trim();
        try {
            List<Reclamation> resultats = titreRecherche.isEmpty()
                    ? reclamationService.recuperer()
                    : reclamationService.rechercherParTitre(titreRecherche);

            afficherReclamations(resultats);
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la recherche : " + e.getMessage());
        }
    }

    private void afficherReclamations(List<Reclamation> reclamations) {
        reclamationGridPane.getChildren().clear();
        int row = 0;
        int col = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Reclamation reclamation : reclamations) {
            VBox card = new VBox();
            card.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 1, 1);");

            Label titreLabel = new Label("Titre : " + reclamation.getTitre());
            Label descriptionLabel = new Label("Description : " + reclamation.getDescription());
            Label statutLabel = new Label("Statut : " + reclamation.getStatus());
            Label dateLabel = new Label("Date : " + (reclamation.getDate() != null ? dateFormat.format(reclamation.getDate()) : "Non définie"));

            int nombreReponses = 0;
            try {
                nombreReponses = reclamationService.compterReponsesParReclamation(reclamation.getId());
            } catch (SQLException e) {
                System.err.println("Erreur lors de la récupération du nombre de réponses : " + e.getMessage());
            }
            Label nombreReponsesLabel = new Label("Réponses : " + nombreReponses);

            Button modifierButton = new Button("Modifier");
            Button supprimerButton = new Button("Supprimer");
            Button voirReponsesButton = new Button("Voir Réponses");

            modifierButton.setOnAction(e -> modifierReclamation(reclamation));
            supprimerButton.setOnAction(e -> supprimerReclamation(reclamation));
            voirReponsesButton.setOnAction(e -> voirReponses(reclamation));

            card.getChildren().addAll(titreLabel, descriptionLabel, statutLabel, dateLabel, nombreReponsesLabel, modifierButton, supprimerButton, voirReponsesButton);
            reclamationGridPane.add(card, col, row);

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }
    }


    @FXML
    public void ajouterReclamation() {
        try {
            MainFX.openAjouterReclamationWindow((Stage) ajouterButton.getScene().getWindow(), utilisateur, mission);
            chargerReclamations();
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de la réclamation : " + e.getMessage());
        }
    }

    private void modifierReclamation(Reclamation reclamation) {
        try {
            MainFX.openModifierReclamationWindow((Stage) ajouterButton.getScene().getWindow(), reclamation, utilisateur, mission);
            chargerReclamations();
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture de la fenêtre de modification : " + e.getMessage());
        }
    }

    private void supprimerReclamation(Reclamation reclamation) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette réclamation ?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    reclamationService.supprimer(reclamation);
                    chargerReclamations();
                    afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Réclamation supprimée avec succès !");
                } catch (SQLException e) {
                    afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de la réclamation : " + e.getMessage());
                }
            }
        });
    }

    private void voirReponses(Reclamation reclamation) {
        try {
            if (utilisateur == null) {
                System.out.println("⚠️ Utilisateur non défini. Assignation d'un utilisateur par défaut.");
                utilisateur = new User(1, "Nom", "Prénom", "email@domain.com", "password", "freelance");
            }

            MainFX.openReponsesWindow((Stage) ajouterButton.getScene().getWindow(), reclamation, utilisateur);
            rafraichirReclamation(reclamation);
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture de la fenêtre des réponses : " + e.getMessage());
        }
    }


    private void rafraichirReclamation(Reclamation reclamation) {
        try {
            int nombreReponses = reclamationService.compterReponsesParReclamation(reclamation.getId());

            for (javafx.scene.Node node : reclamationGridPane.getChildren()) {
                if (node instanceof VBox) {
                    VBox card = (VBox) node;
                    Label titreLabel = (Label) card.getChildren().get(0);

                    if (titreLabel.getText().equals("Titre : " + reclamation.getTitre())) {
                        Label nombreReponsesLabel = (Label) card.getChildren().get(4);
                        nombreReponsesLabel.setText("Réponses : " + nombreReponses);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour du nombre de réponses : " + e.getMessage());
        }
    }



    private void trierParNombreDeReponses() {
        try {
            List<Reclamation> reclamationsTriees = reclamationService.recupererTrieParReponses()
                    .stream()
                    .sorted(Comparator.comparingInt(Reclamation::getNombreReponses).reversed())
                    .collect(Collectors.toList());

            if (!isSortedAsc) {
                Collections.reverse(reclamationsTriees);
            }
            isSortedAsc = !isSortedAsc;
            afficherReclamations(reclamationsTriees);
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors du tri des réclamations : " + e.getMessage());
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
