package org.example.Controller;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.example.Model.Post;
import org.example.Util.DatabaseConnection;
import org.example.Service.PostService;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PostController {
    private final Connection cnx2;
    int userId = 1;
    @FXML
    private TextArea text;
    @FXML
    private TextField imageUrlField;
    @FXML
    private ImageView imageView ;

    private String selectedImagePath ;
   @FXML private Button removeImageButton;

    PostService postService = new PostService();

    public PostController() {
        cnx2 = DatabaseConnection.getInstance().getCnx();
    }


    @FXML
    private void handleAddPost(){
        int UserId = 1;
        String title = "titre";
        String content = text.getText();
        String imageUrl = imageUrlField.getText();
        String categorie = "formation";
        Post post = new Post(UserId,imageUrl,title,categorie,content);
        PostService postService = new PostService();
        postService.PostAdd(post);
    }
    @FXML
    private void handleDeletePost() {
        postService.PostDelete(20);
    }
    @FXML
    private void handleEditPost() {
        PostService postService = new PostService();
        postService.PostUp(12);
    }
    @FXML
    private void AddPicture(){
        FileChooser fileChooser = new FileChooser();

        // Définir un filtre pour n'afficher que les fichiers image
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images (*.jpg, *.png, *.jpeg)", "*.jpg", "*.png", "*.jpeg")
        );

        // Ouvrir le sélecteur de fichiers
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            // Convertir le fichier en chemin URL
            String imageUrl = file.toURI().toString();
            imageUrlField.setText(imageUrl);
            imageView.setImage(new Image(imageUrl));
            removeImageButton.setVisible(true);// Afficher l'image sélectionnée
        }
    }
    @FXML
    public void handleUpdatePost(){
        postService.PostUp(6);
    }

    public void PostUp(int PostId ){
        String title = "Up";
        String content =  "updated";
        String imageUrl = (selectedImagePath != null) ? selectedImagePath : imageUrlField.getText();
        String categorie = "formation";

        String query = "UPDATE posts SET title = ?, content = ?, image_url = ?, categorie = ? WHERE post_id = ?";

        try (PreparedStatement st = cnx2.prepareStatement(query)) {
            st.setString(1, title);
            st.setString(2, content);
            st.setString(3, imageUrl);
            st.setString(4, categorie);
            st.setInt(5, PostId);

            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Post mis à jour avec succès !");
            } else {
                showAlert(Alert.AlertType.WARNING, "Aucune modification", "Aucun post mis à jour.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de mettre à jour le post.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    @FXML
    private void handleRemoveImage(){
        imageView.setImage(null);
        imageView.setVisible(false);
        removeImageButton.setVisible(false);
    }


}


