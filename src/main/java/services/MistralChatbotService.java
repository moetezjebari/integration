package services;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class MistralChatbotService {
    private static final String API_URL = "https://api.mistral.ai/v1/chat/completions";
    private static final String API_KEY = "63sbIxjdxrM4pmyD09l9yZlBYQSBFIE1"; // Mets ici ta clé API Mistral AI

    private final OkHttpClient client = new OkHttpClient();

    public String getResponse(String userMessage) throws IOException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "mistral-medium"); // Remplace par le modèle disponible si nécessaire
        jsonBody.put("messages", new JSONObject[]{ new JSONObject().put("role", "user").put("content", userMessage) });

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erreur API: " + response.code() + " " + response.message());
            }

            JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        }
    }
}
