package org.example.Model;

public class Comment_reactions {
    private int reaction_id ;
    private int comment_id ;
    private int user_id ;
    private String reaction ;
    private String date ;


    // constructeur de recuperation   :
    public Comment_reactions(int reaction_id, int comment_id, int user_id, String reaction, String date) {
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.reaction = reaction;
        this.date = date;
        this.reaction_id = reaction_id;
    }
    // constructeur pour l ajout
    public Comment_reactions(int comment_id , int user_id , String reaction ){
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.reaction = reaction;
    }

    public int getReaction_id() {return reaction_id;}
    public int getComment_id() {return comment_id;}
    public int getUser_id() {return user_id;}
    public String getReaction() {return reaction;}
    public String getDate() {return date;}

    public void setReaction_id(int reaction_id) {this.reaction_id = reaction_id;}
    public void setComment_id(int comment_id) {this.comment_id = comment_id;}
    public void setUser_id(int user_id) {this.user_id = user_id;}
    public void setReaction(String reaction) {this.reaction = reaction;}
    public void setDate(String date) {this.date = date;}

}
