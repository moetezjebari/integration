package org.example.Service;
import org.example.Util.DatabaseConnection;
import org.example.Model.Comment_reactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ReactionCommentService {
     private final Connection cnx2 ;

     public ReactionCommentService(){cnx2 = DatabaseConnection.getInstance().getCnx();}

    public void Reaction_add(Comment_reactions reaction) {
        String query = "INSERT INTO comment_reactions (comment_id, user_id , reaction_type , created_at) VALUES (?,?,?,NOW())";

        try {
            PreparedStatement st = cnx2.prepareStatement(query);
            st.setInt(1, reaction.getComment_id());
            st.setInt(2, reaction.getUser_id());
            st.setString(3,reaction.getReaction());
            int row = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Reaction_Delete(int comment_id , int user_id) {
         String query = "DELETE FROM comment_reactions WHERE comment_id = ? AND user_id = ?";
         try{
             PreparedStatement st = cnx2.prepareStatement(query);
             st.setInt(1, comment_id);
             st.setInt(2, user_id);
             int row = st.executeUpdate();
         }catch(Exception e){
             e.printStackTrace();
         }
    }
    public Comment_reactions getReactionByUser(int user_id , int comment_id) {
         String query = "SELECT * FROM comment_reactions WHERE user_id = ? AND comment_id = ?";
         try{
             PreparedStatement st = cnx2.prepareStatement(query);
             st.setInt(1, user_id);
             st.setInt(2, comment_id);
             ResultSet rs = st.executeQuery();

                if (rs.next()){
                    return new Comment_reactions(
                            rs.getInt("comment_id"),
                            rs.getInt("user_id"),
                            rs.getString("reaction_type")
                    );
                }
         }catch(Exception e){
             e.printStackTrace();
         }
         return null;
    }
    public void Reaction_Up(int commentId, int currentUserId , String reaction) {
         String query = "UPDATE comment_reactions SET reaction_type = ? WHERE comment_id = ? AND user_id = ?";
         try {
             PreparedStatement st = cnx2.prepareStatement(query);
             st.setString(1,reaction);
             st.setInt(2,commentId);
             st.setInt(3,currentUserId);
             int row = st.executeUpdate();
         }catch(Exception e){
             e.printStackTrace();
         }
    }
}

