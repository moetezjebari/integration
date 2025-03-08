package org.example.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class BadwordService {

    public boolean containsBadwords(String text) {

        try {
            URL url = new URL("https://www.purgomalum.com/service/json?text=" + java.net.URLEncoder.encode(text, "UTF-8"));            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            String result = jsonResponse.getString("result");

            // Si le texte original et le résultat filtré sont différents, cela signifie qu'il y a des mots inappropriés
            return !text.equals(result);

        } catch (Exception e) {
            e.printStackTrace();
            return false; // En cas d'erreur, on laisse passer le commentaire
        }
    }
}