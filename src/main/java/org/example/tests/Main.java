package org.example.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.Util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;


public class Main extends Application {

    public void start(Stage primaryStage)  {
        Connection conn = DatabaseConnection.getInstance().getCnx();

        if (conn != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GPublication/fxml/NavBar.fxml"));
            Parent root = null;
            try {
                root = loader.load();
                Scene scene = new Scene(root );
                primaryStage.setTitle("Gestion des Publications");
                primaryStage.setScene(scene);
                primaryStage.show();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
         else {
            System.out.println("❌ Connexion échouée !");
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
