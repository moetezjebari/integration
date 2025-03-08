package services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Properties;

public class MailService {
    private static final String USER = "derouicheroua2@gmail.com"; // Remplace par ton adresse Gmail
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static void envoyerEmail(String destinataire, String sujet, String titre, String description) throws GeneralSecurityException, IOException, MessagingException {
        try {
            // Initialiser le transport sécurisé
            Credential credential = GmailOAuth.getCredentials(GoogleNetHttpTransport.newTrustedTransport());

            // Créer une instance du service Gmail
            Gmail service = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                    .setApplicationName("Mon Application Gmail OAuth2")
                    .build();

            // Configuration de la session email
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USER, "rouaderouiche123"); // Ne pas stocker en dur, utiliser des variables d'environnement
                }
            });

            // Création du contenu de l'email
            String contenu = "Cher(e) Client(e),\n\n" +
                    "Nous avons bien reçu votre réclamation et nous vous remercions de nous avoir contactés.\n\n" +
                    "Détails de votre réclamation :\n" +
                    "- Titre : " + titre + "\n" +
                    "- Description : " + description + "\n" +
                    "- Statut : En attente\n\n" +
                    "Notre équipe traitera votre demande dans les plus brefs délais et reviendra vers vous avec une réponse.\n" +
                    "Si vous avez des questions supplémentaires, n'hésitez pas à nous contacter.\n\n" +
                    "Cordialement,\n" +
                    "L'équipe Freelance Connect";

            // Création de l'email
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(USER));
            email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(destinataire));
            email.setSubject(sujet);
            email.setText(contenu);

            // Convertir l'email au format Gmail API
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

            Message message = new Message();
            message.setRaw(encodedEmail);

            // Envoyer l'email via Gmail API
            service.users().messages().send("me", message).execute();
            System.out.println("✅ Email envoyé avec succès !");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi de l'email : " + e.getMessage());
            throw e;
        }
    }
}
