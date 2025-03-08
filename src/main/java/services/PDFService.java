package services;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PDFService {
    private static final String PDFSHIFT_API_KEY = "sk_bcfea9b1eb1ca0f52b3b37dbb09ee19670971cee"; // Remplacez par votre clé API
    private static final String PDFSHIFT_URL = "https://api.pdfshift.io/v3/convert/pdf";

    /**
     * Génère un PDF à partir d'un contenu HTML ou Markdown.
     *
     * @param content Le contenu HTML ou Markdown.
     * @param outputFilePath Le chemin du fichier PDF à générer.
     */
    public void generatePDF(String content, String outputFilePath) {
        OkHttpClient client = new OkHttpClient();

        // Préparer les données pour la requête
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("source", content);
        requestBody.put("landscape", false); // Orientation portrait
        requestBody.put("format", "A4"); // Format de page

        // Convertir le corps de la requête en JSON avec Gson
        String jsonBody = new Gson().toJson(requestBody);
        System.out.println("Corps de la requête JSON : " + jsonBody);

        // Créer la requête HTTP
        Request request = new Request.Builder()
                .url(PDFSHIFT_URL)
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .addHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((PDFSHIFT_API_KEY + ":").getBytes()))
                .build();

        System.out.println("Envoi de la requête à l'API PDFShift...");

        try (Response response = client.newCall(request).execute()) {
            System.out.println("Réponse reçue de l'API PDFShift. Code : " + response.code());

            if (!response.isSuccessful()) {
                String errorBody = response.body().string();
                System.err.println("Erreur API PDFShift : " + response.code() + " - " + errorBody);
                throw new IOException("Erreur : " + response.code() + " - " + errorBody);
            }

            // Sauvegarder le PDF généré
            System.out.println("Tentative d'écriture du fichier PDF à : " + outputFilePath);
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(response.body().bytes());
                System.out.println("PDF généré avec succès : " + outputFilePath);
            } catch (IOException e) {
                System.err.println("Erreur lors de l'écriture du fichier PDF : " + e.getMessage());
                throw e;
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la génération du PDF : " + e.getMessage());
            e.printStackTrace();
        }
    }
}