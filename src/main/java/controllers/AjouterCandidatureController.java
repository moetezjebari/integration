package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.*;
import services.CandidatureService;
import services.MissionService;
import services.UtilisateurService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class AjouterCandidatureController {
    @FXML
    private ComboBox<Utilisateur> utilisateurComboBox;

    @FXML
    private ComboBox<Mission> missionComboBox;

    @FXML
    private Label nomImageLabel;

    @FXML
    private TextArea reponseQuestionsArea;

    private File imageSelectionnee;
    private CandidatureService candidatureService = new CandidatureService();
    private MissionService missionService = new MissionService();
    private UtilisateurService utilisateurService = new UtilisateurService();

    @FXML
    public void initialize() {
        chargerUtilisateurs();
        chargerMissions();
    }

    private void chargerUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.getAll();
        if (utilisateurs != null && !utilisateurs.isEmpty()) {
            utilisateurComboBox.getItems().addAll(utilisateurs);
            utilisateurComboBox.setCellFactory(param -> new ListCell<Utilisateur>() {
                @Override
                protected void updateItem(Utilisateur utilisateur, boolean empty) {
                    super.updateItem(utilisateur, empty);
                    if (empty || utilisateur == null) {
                        setText(null);
                    } else {
                        setText(utilisateur.getNom() + " " + utilisateur.getPrenom());
                    }
                }
            });
            utilisateurComboBox.setButtonCell(new ListCell<Utilisateur>() {
                @Override
                protected void updateItem(Utilisateur utilisateur, boolean empty) {
                    super.updateItem(utilisateur, empty);
                    if (empty || utilisateur == null) {
                        setText(null);
                    } else {
                        setText(utilisateur.getNom() + " " + utilisateur.getPrenom());
                    }
                }
            });
        } else {
            afficherAlerte("Avertissement", "Aucun utilisateur trouvé", "Veuillez ajouter des utilisateurs avant de créer une candidature.");
        }
    }

    private void chargerMissions() {
        List<Mission> missions = missionService.getAll();
        if (missions != null && !missions.isEmpty()) {
            missionComboBox.getItems().addAll(missions);
            missionComboBox.setCellFactory(param -> new ListCell<Mission>() {
                @Override
                protected void updateItem(Mission mission, boolean empty) {
                    super.updateItem(mission, empty);
                    if (empty || mission == null) {
                        setText(null);
                    } else {
                        setText(mission.getTitre() + " - " + mission.getNomEntreprise());
                    }
                }
            });
            missionComboBox.setButtonCell(new ListCell<Mission>() {
                @Override
                protected void updateItem(Mission mission, boolean empty) {
                    super.updateItem(mission, empty);
                    if (empty || mission == null) {
                        setText(null);
                    } else {
                        setText(mission.getTitre() + " - " + mission.getNomEntreprise());
                    }
                }
            });
        } else {
            afficherAlerte("Avertissement", "Aucune mission trouvée", "Veuillez ajouter des missions avant de créer une candidature.");
        }
    }

    @FXML
    public void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        imageSelectionnee = fileChooser.showOpenDialog(new Stage());

        if (imageSelectionnee != null) {
            nomImageLabel.setText(imageSelectionnee.getName());
        } else {
            nomImageLabel.setText("Aucune image sélectionnée");
        }
    }

    @FXML
    public void ajouterCandidature() {
        Utilisateur utilisateur = utilisateurComboBox.getValue();
        Mission mission = missionComboBox.getValue();
        String reponseQuestions = reponseQuestionsArea.getText();

        if (utilisateur == null || mission == null || imageSelectionnee == null || reponseQuestions.isEmpty()) {
            afficherAlerte("Erreur", "Champs manquants", "Veuillez remplir tous les champs et sélectionner une image.");
            return;
        }

        String cheminImage = imageSelectionnee.getAbsolutePath();

        // Générer la lettre de motivation
        String lettreMotivation = LettreMotivationGenerator.genererLettreMotivation(
                new Candidature(0, utilisateur, mission, cheminImage, reponseQuestions, null)
        );

        Candidature nouvelleCandidature = new Candidature(
                0, utilisateur, mission, cheminImage, reponseQuestions, lettreMotivation
        );

        boolean result = candidatureService.add(nouvelleCandidature);
        if (result) {
            // Envoyer un e-mail de confirmation
            envoyerEmail(utilisateur.getEmail(), "Confirmation de candidature",
                    "Vous avez postulé avec succès pour la mission : " + mission.getTitre());

            afficherAlerte("Succès", "Candidature ajoutée", "La candidature a été ajoutée avec succès.");
            reinitialiserFormulaire();
        } else {
            afficherAlerte("Erreur", "Échec de l'ajout", "Une erreur s'est produite lors de l'ajout de la candidature.");
        }
    }

    @FXML
    public void envoyerEmailTest() {
        Utilisateur utilisateur = utilisateurComboBox.getValue();
        if (utilisateur == null) {
            afficherAlerte("Erreur", "Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur.");
            return;
        }

        String destinataire = utilisateur.getEmail();
        String sujet = "Test d'envoi d'e-mail";
        String corps = "Ceci est un e-mail de test pour vérifier l'envoi d'e-mails depuis l'application.";

        envoyerEmail(destinataire, sujet, corps);
    }

    private void envoyerEmail(String destinataire, String sujet, String corps) {
        final String username = "chiraz.wesleti@etudiant-fst.utm.tn"; // Remplacez par votre adresse e-mail
        final String password = "12345678"; // Remplacez par votre mot de passe

        // Configuration des propriétés SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Serveur SMTP de Gmail
        props.put("mail.smtp.port", "587"); // Port SMTP de Gmail
        props.put("mail.smtp.ssl.trust", "*");

        // Création de la session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setText(corps);

            // Envoi de l'e-mail
            Transport.send(message);
            System.out.println("E-mail envoyé avec succès.");
            afficherAlerte("Succès", "E-mail envoyé", "Objet : Confirmation d'envoi de votre candidature\n" +
                    "\n" +
                    "Bonjour Chiraz,\n" +
                    "\n" +
                    "Nous vous confirmons que votre candidature pour la mission Application IOT a bien été envoyée avec succès à Actia.\n" +
                    "\n" +
                    "Le recruteur examinera votre profil et reviendra vers vous si votre candidature correspond aux attentes du poste.\n" +
                    "\n" +
                    "Restant à votre disposition pour toute question, nous vous souhaitons bonne chance pour la suite du processus de sélection.\n" +
                    "\n" +
                    "Cordialement,\n" +
                    "L’équipe de FreelanceConnect " + destinataire);
        } catch (MessagingException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Échec d'envoi de l'e-mail", "Impossible d'envoyer l'e-mail.");
        }
    }

    @FXML
    public void genererLettreMotivationPDF() {
        Utilisateur utilisateur = utilisateurComboBox.getValue();
        Mission mission = missionComboBox.getValue();
        String reponseQuestions = reponseQuestionsArea.getText();

        if (utilisateur == null || mission == null || reponseQuestions.isEmpty()) {
            afficherAlerte("Erreur", "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // Générer la lettre de motivation
        String lettreMotivation = LettreMotivationGenerator.genererLettreMotivation(
                new Candidature(0, utilisateur, mission, null, reponseQuestions, null)
        );

        // Générer le PDF
        String filePath = "lettre_motivation.pdf";
        PDFGenerator.generateLettreMotivationPDF(lettreMotivation, filePath);

        // Ouvrir le PDF après sa génération
        ouvrirFichierPDF(filePath);

        // Afficher un message de succès
        afficherAlerte("Succès", "PDF généré", "La lettre de motivation a été générée avec succès : " + filePath);
    }

    private void ouvrirFichierPDF(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file); // Ouvrir le fichier avec l'application par défaut
            } else {
                afficherAlerte("Erreur", "Fichier introuvable", "Le fichier PDF n'a pas pu être trouvé.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible d'ouvrir le fichier", "Une erreur s'est produite lors de l'ouverture du fichier PDF.");
        }
    }

    @FXML
    public void annulerAjout() {
        Stage stage = (Stage) utilisateurComboBox.getScene().getWindow();
        stage.close();
    }

    private void afficherAlerte(String titre, String entete, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    private void reinitialiserFormulaire() {
        utilisateurComboBox.getSelectionModel().clearSelection();
        missionComboBox.getSelectionModel().clearSelection();
        nomImageLabel.setText("Aucune image sélectionnée");
        reponseQuestionsArea.clear();
        imageSelectionnee = null;
    }
}