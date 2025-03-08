package org.example.Model;

public class Post_reactions {
     private int post_id;
     private int user_id;
     private int reaction_id;
     private String reaction;
     private String date ;

// constructeur pour l ajout
    public Post_reactions(int post_id, int user_id, String reaction) {
      this.post_id = post_id;
      this.user_id = user_id;
      this.reaction = reaction;
    }
    // pout recuperer
    public Post_reactions(int post_id, int user_id, int reaction_id , String reaction , String date ) {
            this.post_id = post_id;
            this.user_id = user_id;
            this.reaction_id = reaction_id;
            this.reaction = reaction;
            this.date = date  ;
    }

    // getters
    public int getPost_id() {return post_id;}
    public int getUser_id() {return user_id;}
    public int getReaction_id() {return reaction_id;}
    public String getReaction() {return reaction;}
    public String getDate() {return date;}


    //setters
    public void setPost_id(int post_id) {this.post_id = post_id;}
    public void setUser_id(int user_id) {this.user_id = user_id;}
    public void setReaction_id(int reaction_id) {this.reaction_id = reaction_id;}
    public void setReaction(String reaction) {this.reaction = reaction;}
    public void setDate(String date) {this.date = date;}



}
