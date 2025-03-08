package org.example.Controller;
import com.mysql.cj.exceptions.StreamingNotifiable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.example.Model.Post;
import org.example.Service.PostService;

import java.util.List;
import java.util.stream.Collectors;


public class NavBarController {
    private static  VBox currentActive = null;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private TextField input_recherche ;
    private Popup suggestionsPopup = new Popup();
    private ListView<String> suggestionsList = new ListView<>();
    private boolean popupShown = false;


    public  void initialize() {
        suggestionsPopup.setAutoHide(true);
        suggestionsList.setPrefWidth(300);
        suggestionsList.setPrefHeight(200);
        suggestionsPopup.getContent().add(suggestionsList);


        input_recherche.textProperty().addListener((observable ,oldValue , newValue)->{
            if (newValue != null && !newValue.trim().isEmpty()) {

                // Rechercher les titres correspondants
                List<Post> matchingPosts = PostService.searchPostsByTitle(newValue);

                // Extraire uniquement les titres
                List<String> titles = matchingPosts.stream()
                        .map(Post::getTitle)
                        .collect(Collectors.toList());

                // Mettre à jour la liste de suggestions
                ObservableList<String> items = FXCollections.observableArrayList(titles);
                suggestionsList.setItems(items);
                // Afficher la popup si elle contient des éléments
                if (!titles.isEmpty()) {
                    if (!popupShown) {
                        Bounds bounds = input_recherche.localToScreen(input_recherche.getBoundsInLocal());
                        suggestionsPopup.show(input_recherche,
                                bounds.getMinX(),
                                bounds.getMaxY());
                        popupShown = true;
                    }
                } else {
                    suggestionsPopup.hide();
                    popupShown = false;
                }
            } else {
                // Cacher la liste si le champ est vide
                suggestionsPopup.hide();
                popupShown = false;
            }
        });
    }

    private void setActive(VBox clicked) {
        // Retire la classe active de l'élément précédent
        if (currentActive != null) {
            currentActive.getStyleClass().remove("active");

        }
        // Ajoute la classe active au nouvel élément
        clicked.getStyleClass().add("active");
        currentActive = clicked;
        String rout = getCurrentActive(currentActive.getId()) ;
        shownotifications(rout);

    }
    @FXML private VBox retour  ;
    @FXML
    private void retour(){
        try {
            // Charger la nouvelle scène depuis le fichier FXML (home.fxml)
            Parent newRoot = FXMLLoader.load(getClass().getResource("/Dashboard.fxml"));
            Scene newScene = new Scene(newRoot );

            // Changer la scène actuelle
              Stage stage = (Stage) currentActive.getScene().getWindow();
            //Stage stage = new Stage();
            stage.setScene(newScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Vérifiez la console pour des détails d'erreur
        }
    }

    @FXML
    private void handleNavClick(MouseEvent event) {
        setActive((VBox) event.getSource());
    }
    public String getCurrentActive(String clicked) {
        switch (clicked){
            case "acceuil":
                return "acceuil";
            case "notif":
                return "Notifications";
            default:
                return "MainView";
        }
    }

    @FXML
    private void shownotifications(String rout) {
    try {
        // Charger la nouvelle scène depuis le fichier FXML (home.fxml)
        Parent newRoot = FXMLLoader.load(getClass().getResource("/GPublication/fxml/" + rout + ".fxml"));
        PostInterfaceController postInterfaceController = new FXMLLoader(getClass().getResource("/GPublication/fxml/PostInterface.fxml")).getController();
       System.out.println("hello");
        StackPane contentArea = (StackPane) currentActive.getScene().lookup("#contentArea");
contentArea.getChildren().clear();
contentArea.getChildren().add(newRoot);
        //Scene newScene = new Scene(newRoot );

        // Changer la scène actuelle
       //  Stage stage = (Stage) currentActive.getScene().getWindow();
        //Stage stage = new Stage();
        //stage.setScene(newScene);
        //stage.show();
    } catch (Exception e) {
        e.printStackTrace(); // Vérifiez la console pour des détails d'erreur
    }
    }



}
