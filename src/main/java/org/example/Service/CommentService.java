package org.example.Service;

import javafx.scene.control.Alert;
import org.example.Model.Comments;
import org.example.Model.Notification;
import org.example.Util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class CommentService {
 String currentUsername = "moetez ";
    private final Connection cnx2;
    public CommentService() {
        cnx2 = DatabaseConnection.getInstance().getCnx();
    }
int currentUserId=1;
    public void AddComment(Comments comment ){
            String query = "INSERT INTO comments (post_id, user_id, comment_text, created_at) VALUES (?, ?, ?, NOW())";
            try  {
                PreparedStatement st = cnx2.prepareStatement(query);
                System.out.println("click");
                st.setInt(1, comment.getPost_id());
                st.setInt(2, comment.getUser_id());
                st.setString(3, comment.getComment_text());

                int rowsInserted = st.executeUpdate();
                String username_Owner = getOwnerPostUserName(comment.getPost_id());

              if(!username_Owner.equals(currentUsername)) {
                  System.out.println(username_Owner);

                           String title = "nouveau commntaire ";
                           String message = currentUsername + "a commenter votre publicaton ";
                  NotificationService notificationService = new NotificationService();
                  Notification notification = new Notification(destinataire(comment.getPost_id()),currentUserId,title,message,false,"comment", comment.getPost_id());
                  notificationService.pushNotification(notification);
              }
                if (rowsInserted > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Commentaire ajouté avec succès !");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Erreur SQL : " + e.getMessage());
                showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter le commentaire.");
            }
        }
        public int destinataire(int postId) throws SQLException{
          String query = " SELECT user_id FROM posts WHERE post_id = ?";
          PreparedStatement st = cnx2.prepareStatement(query);
          st.setInt(1, postId);
          ResultSet rs = st.executeQuery();
          if ( rs.next()){
              return rs.getInt("user_id");
          }
        return -1;
        }

   private String getOwnerPostUserName(int postId) throws SQLException {
             String query ="SELECT  u.username " +
                     "FROM users u " +
                     "JOIN posts p ON u.user_id=p.user_id " +
                     "WHERE p.post_id=?";

             PreparedStatement st = cnx2.prepareStatement(query);
             st.setInt(1, postId);
             ResultSet rs = st.executeQuery();
             if (rs.next()){
                 return rs.getString("username");
             }

        return "Anonyme" ;
   }

    //Read Comments
    public  List<Comments> getCommentsByPostId(int postId) {

      List<Comments> comments = new ArrayList<>();
      String query = "SELECT * FROM comments WHERE post_id = ?";

      try  {
          PreparedStatement stmt = cnx2.prepareStatement(query);

          stmt.setInt(1, postId);
          ResultSet rs = stmt.executeQuery();

          while (rs.next()) {

              comments.add(new Comments(
                      rs.getInt("comment_id"),
                      rs.getInt("post_id"),
                      rs.getInt("user_id"),
                      rs.getString("comment_text"),
                      rs.getString("created_at"),
                      rs.getInt("parent_id")
              ));
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return comments;
  }
    public void DeleteSpecificComment(int commentId) {
        try {

            String d_r = "DELETE FROM comments WHERE parent_id = ?";
            PreparedStatement stmt = cnx2.prepareStatement(d_r);
            stmt.setInt(1, commentId);
            stmt.executeUpdate();

            String query = "DELETE FROM comments WHERE comment_id = ?";
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setInt(1, commentId);
            pst.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Commentaire supprimé avec succès");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression du commentaire");
            e.printStackTrace();
        }
    }
    public void UpdateSpecificComment(int commentId, String newText) {
        try {
            String query = "UPDATE comments SET comment_text = ? WHERE comment_id = ?";
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setString(1, newText);
            pst.setInt(2, commentId);
            pst.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Commentaire modifié avec succès");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification du commentaire");
            e.printStackTrace();
        }
    }
    public void AddResponse(Comments response ){

          String query = "INSERT INTO comments (post_id , user_id , comment_text , created_at ,parent_id ) VALUES (?,?,?,NOW(),?)";
          try {
               PreparedStatement st = cnx2.prepareStatement(query);
               st.setInt(1, response.getPost_id());
               st.setInt(2, response.getUser_id());
               st.setString(3, response.getComment_text());
               st.setInt(4, response.getParent_id());
               int rowsInserted = st.executeUpdate();

          }catch(Exception e){
              e.printStackTrace();
          }
    }
    public List<Comments> showResponses(int comment_id){
        String query = "SELECT * FROM comments WHERE parent_id = ?";
        List<Comments> responses = new ArrayList<>();
        try {  PreparedStatement st = cnx2.prepareStatement(query);
            st.setInt(1, comment_id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                 responses.add(new Comments(
                         rs.getInt("comment_id"),
                         rs.getInt("post_id"),
                         rs.getInt("user_id"),
                         rs.getString("comment_text"),
                         rs.getString("created_at"),
                         rs.getInt("parent_id")
                         ));
            }
            return responses;


        }catch(Exception e){
            e.printStackTrace();
        }
        return null ;
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
