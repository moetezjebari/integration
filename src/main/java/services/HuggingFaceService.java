package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class HuggingFaceService {
    private static final String HUGGING_FACE_API_KEY = "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"; // Remplacez par votre token Hugging Face
    private static final String MODEL_URL = "https://api-inference.huggingface.co/models/gpt2"; // Modèle GPT-2 pour tester

    public String generateText(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        // Construire le corps de la requête pour Hugging Face
        String jsonInput = String.format("{\"inputs\": \"%s\"}", prompt);

        RequestBody body = RequestBody.create(jsonInput, JSON);
        Request request = new Request.Builder()
                .url(MODEL_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + HUGGING_FACE_API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body().string();
                System.err.println("Erreur : " + response.code() + " - " + errorBody);
                throw new IOException("Erreur : " + response.code() + " - " + errorBody);
            }
            String responseBody = response.body().string();
            System.out.println("Réponse de l'API : " + responseBody); // Log de la réponse
            return extractGeneratedText(responseBody);
        }
    }

    // Méthode pour extraire le texte généré de la réponse JSON
    private String extractGeneratedText(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            return rootNode.get(0).get("generated_text").asText();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'extraction du texte généré : " + e.getMessage());
        }
    }
}