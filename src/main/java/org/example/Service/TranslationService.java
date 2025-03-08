package org.example.Service;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.example.Util.DatabaseConnection;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationService {

    private final Connection cnx2;

    public TranslationService() {
        cnx2 = DatabaseConnection.getInstance().getCnx();
    }

    /**
     * Détecte la langue du texte et traduit automatiquement vers l'autre langue (FR/EN)
     * @param text Le texte à traduire
     * @return Le texte traduit ou le texte original en cas d'erreur
     */
    public String autoTranslateText(String text) {
        // Détecte si le texte est principalement en français ou en anglais
        String detectedLang = detectLanguage(text);

        // Détermine la langue cible (si français détecté -> anglais, sinon -> français)
        String targetLang = "fr".equals(detectedLang) ? "en" : "fr";

        // Traduit le texte
        return translateText(text, detectedLang, targetLang);
    }

    /**
     * Détecte de manière simple si un texte est en français ou en anglais
     * Cette méthode utilise une approche basique basée sur des mots fréquents
     * @param text Le texte à analyser
     * @return "fr" si le texte semble être en français, "en" sinon
     */
    private String detectLanguage(String text) {
        // Convertir en minuscules pour la comparaison
        String lowerText = text.toLowerCase();

        // Mots fréquents en français (articles, prépositions, etc.)
        String[] frenchWords = {"le", "la", "les", "un", "une", "des", "et", "ou", "mais", "donc",
                "car", "pour", "dans", "avec", "sur", "ce", "cette", "ces", "mon",
                "ton", "son", "notre", "votre", "leur", "je", "tu", "il", "elle",
                "nous", "vous", "ils", "elles", "est", "sont", "être", "avoir","mauvais" ,"bonne","geniale","bravo","merci"};

        // Mots fréquents en anglais
        String[] englishWords = {"the", "a", "an", "and", "or", "but", "for", "in", "on", "at",
                "with", "by", "to", "of", "from", "this", "that", "these", "those",
                "my", "your", "his", "her", "our", "their", "i", "you", "he", "she",
                "we", "they", "is", "are", "be", "have"};

        int frenchCount = 0;
        int englishCount = 0;

        // Compter les occurrences de mots français
        for (String word : frenchWords) {
            Pattern pattern = Pattern.compile("\\b" + word + "\\b");
            Matcher matcher = pattern.matcher(lowerText);

            while (matcher.find()) {
                frenchCount++;
            }
        }

        // Compter les occurrences de mots anglais
        for (String word : englishWords) {
            Pattern pattern = Pattern.compile("\\b" + word + "\\b");
            Matcher matcher = pattern.matcher(lowerText);

            while (matcher.find()) {
                englishCount++;
            }
        }

        // Si plus de mots français sont détectés, considérer comme français
        return (frenchCount > englishCount) ? "fr" : "en";
    }

    /**
     * Traduit un texte en utilisant l'API MyMemory
     * @param text Le texte à traduire
     * @param sourceLang La langue source (ex: "fr", "en", "es", etc.)
     * @param targetLang La langue cible (ex: "fr", "en", "es", etc.)
     * @return Le texte traduit ou le texte original en cas d'erreur
     */
    public String translateText(String text, String sourceLang, String targetLang) {
        try {
            // Utiliser l'API MyMemory qui est gratuite et ne nécessite pas de clé API pour un usage limité
            String encodedText = java.net.URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
            URL url = new URL("https://api.mymemory.translated.net/get?q=" + encodedText + "&langpair=" + sourceLang + "|" + targetLang);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Lisez la réponse
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // Analysez la réponse JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject responseData = jsonResponse.getJSONObject("responseData");
            String translatedText = responseData.getString("translatedText");

            return translatedText;

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de traduction",
                    "Impossible de traduire le texte: " + e.getMessage());
            return text; // Retourne le texte original en cas d'erreur
        }
    }

    /**
     * Traduit automatiquement un texte et met à jour un Label avec le résultat
     * @param contentLabel Le Label à mettre à jour
     * @param originalText Le texte à traduire
     */
    public void autoTranslateAndUpdateLabel(Label contentLabel, String originalText) {
        // Créer un thread séparé pour ne pas bloquer l'interface utilisateur
        new Thread(() -> {
            String translatedText = autoTranslateText(originalText);

            // Mettre à jour l'interface utilisateur dans le thread JavaFX
            Platform.runLater(() -> {
                contentLabel.setText(translatedText);
            });
        }).start();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}