package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import services.MistralChatbotService;

import java.io.IOException;

public class ChatController {
    private final MistralChatbotService chatbotService = new MistralChatbotService();

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    @FXML
    private void envoyerMessage() {
        String userMessage = inputField.getText().trim();
        if (!userMessage.isEmpty()) {
            chatArea.appendText("Vous: " + userMessage + "\n");
            inputField.clear();

            try {
                String response = chatbotService.getResponse(userMessage);
                chatArea.appendText("Bot: " + response + "\n");
            } catch (IOException e) {
                chatArea.appendText("Erreur API: " + e.getMessage() + "\n");
            }
        }
    }
}
