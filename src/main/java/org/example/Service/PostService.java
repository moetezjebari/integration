package org.example.Service;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.Model.Post;
import org.example.Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostService {

   private  final Connection cnx2;
    @FXML
    private TextField imageUrlField;
    private String selectedImagePath ;

    public PostService() {
        cnx2 = DatabaseConnection.getInstance().getCnx();
    }


    public static List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM posts";

        try (Connection conn = DatabaseConnection.getInstance().getCnx();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                posts.add(new Post(
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getString("image_url"),
                        rs.getString("title"),
                        rs.getString("categorie"),
                        rs.getString("content"),
                        rs.getString("created_at")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }
    public void PostAdd(Post post) {
        String query = "INSERT INTO posts (user_id, title, content, image_url, created_at, categorie) VALUES (?, ?, ?, ?, NOW(), ?)";
        try  {
            PreparedStatement st = cnx2.prepareStatement(query);
            System.out.println("click");
            st.setInt(1, post.getUserId());
            st.setString(2, post.getTitle());
            st.setString(3, post.getContent());
            st.setString(4, post.getImage());
            st.setString(5, post.getCategory());

            int rowsInserted = st.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Post ajouté avec succès !");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter le post.");
        }



    }
    public void PostDelete(int PostId) {
        // Première requête pour supprimer les commentaires
        String deleteCommentsQuery = "DELETE FROM comments WHERE post_id = ?";
        // Deuxième requête pour supprimer le post
        String deletePostQuery = "DELETE FROM posts WHERE post_id = ?";

        try {
            // Commencer une transaction
            cnx2.setAutoCommit(false);

            // Supprimer d'abord les commentaires
            try (PreparedStatement stmtComments = cnx2.prepareStatement(deleteCommentsQuery)) {
                stmtComments.setInt(1, PostId);
                stmtComments.executeUpdate();
            }

            // Ensuite supprimer le post
            try (PreparedStatement stmtPost = cnx2.prepareStatement(deletePostQuery)) {
                stmtPost.setInt(1, PostId);
                int rowsDeleted = stmtPost.executeUpdate();

                if (rowsDeleted > 0) {
                    // Si tout s'est bien passé, on valide la transaction
                    cnx2.commit();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Post et commentaires associés supprimés avec succès");
                } else {
                    // Si le post n'existe pas, on annule la transaction
                    cnx2.rollback();
                    showAlert(Alert.AlertType.WARNING, "Erreur", "Post non existant");
                }
            }

        } catch (SQLException e) {
            try {
                // En cas d'erreur, on annule la transaction
                cnx2.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "SQL ERROR", "Impossible de supprimer le post et ses commentaires.");
        } finally {
            try {
                // Remettre autoCommit à true
                cnx2.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void PostUp(int PostId ){
        String title = "Up";
        String content =  "updated";
        String categorie = "formation";

        String query = "UPDATE posts SET title = ?, content = ?,  categorie = ? WHERE post_id = ?";

        try  {
            PreparedStatement st = cnx2.prepareStatement(query);
            st.setString(1, title);
            st.setString(2, content);
            st.setString(3, categorie);
            st.setInt(4, PostId);

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
    public static List<Post> Tri_Post(){
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM posts ORDER BY created_at DESC";
        try{
            Connection conn = DatabaseConnection.getInstance().getCnx();
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                   posts.add(new Post(
                           rs.getInt("post_id"),
                           rs.getInt("user_id"),
                           rs.getString("image_url"),
                           rs.getString("title"),
                           rs.getString("categorie"),
                           rs.getString("content"),
                           rs.getString("created_at")

                   ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return posts;
    }
    /**
     * Recherche des posts par titre
     * @param searchTerm Le terme de recherche
     * @return Liste des posts correspondant au terme de recherche
     */
    public static List<Post> searchPostsByTitle(String searchTerm) {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM posts WHERE categorie LIKE ?";

        try (Connection conn = DatabaseConnection.getInstance().getCnx();
             PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setString(1, searchTerm);
            ResultSet rs = ps.executeQuery();
            try {
                while (rs.next()) {
                    Post post = new Post(
                            rs.getInt("post_id"),
                            rs.getInt("user_id"),
                            rs.getString("image_url"),
                            rs.getString("title"),
                            rs.getString("categorie"),
                            rs.getString("content"),
                            rs.getString("created_at")
                    );
                    posts.add(post);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return posts;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}