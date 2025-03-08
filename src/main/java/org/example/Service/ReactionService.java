package org.example.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import org.example.Model.Post_reactions;
import org.example.Util.DatabaseConnection;

public class ReactionService {

    private final Connection cnx2  ;

    public ReactionService() {cnx2 = DatabaseConnection.getInstance().getCnx();}

    public Post_reactions getUserReaction(int postId, int userId) {
                String query = "SELECT * FROM post_reactions WHERE post_id = ? AND user_id = ?";
                try (PreparedStatement pst = cnx2.prepareStatement(query)) {
                        pst.setInt(1, postId);
                        pst.setInt(2, userId);
                        ResultSet rs = pst.executeQuery();

                        if (rs.next()) {
                                return new Post_reactions(
                                            rs.getInt("post_id"),
                                            rs.getInt("user_id"),
                                            rs.getString("reaction_type")
                                        );
                            }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                return null;
            }
    public void Reaction_add (Post_reactions reaction){
           String query = "INSERT INTO post_reactions (post_id ,user_id ,reaction_type ,created_at) VALUES (?,?,?,NOW())";
           try {
                PreparedStatement st = cnx2.prepareStatement(query);
                st.setInt(1, reaction.getPost_id());
                st.setInt(2, reaction.getUser_id());
                st.setString(3,reaction.getReaction());
               int rowsInserted = st.executeUpdate();
           }catch (SQLException e) {
               e.printStackTrace();
               showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter la Reaction.");
           }
    }
    public void Reaction_delete (int post_id , int user_id){
        String query = "DELETE FROM post_reactions WHERE post_id =? AND user_id =?";
        try {
               PreparedStatement st = cnx2.prepareStatement(query);
               st.setInt(1, post_id);
               st.setInt(2, user_id);
              int r =  st.executeUpdate();
              if ( r>0){showAlert(Alert.AlertType.INFORMATION,"Suuccès","Reaction deleted ");}
        }catch(SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de supprimer  la Reaction.");
        }

    }
    public void Reaction_update (Post_reactions reaction){
        String query = "UPDATE post_reactions SET reaction_type = ? WHERE post_id = ? AND user_id = ?";
        try{ PreparedStatement st = cnx2.prepareStatement(query);
            st.setString(1, reaction.getReaction());
            st.setInt(2, reaction.getPost_id());
            st.setInt(3, reaction.getUser_id());
            st.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"ERROR" , "erreur modifier la reaction ");
        }

    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




}
