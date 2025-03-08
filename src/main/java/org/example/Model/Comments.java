package org.example.Model;

public class Comments {
    private int comment_id ;
    private int post_id  ;
    private int user_id ;
    private int parent_id ;
    private String comment_text ;
    private String date  ;


   // constructeur pour recuperer un Comment
    public Comments(int comment_id, int post_id, int user_id, String comment_text, String date , int parent_id) {
        this.comment_id = comment_id ;
        this.post_id = post_id ;
        this.user_id = user_id ;
        this.comment_text = comment_text ;
        this.date = date ;
        this.parent_id = parent_id ;

    }
    // constructeur pour l ajout du comment
    public Comments(int post_id, int user_id , String comment_text ) {
        this.post_id = post_id ;
        this.user_id = user_id ;
        this.comment_text = comment_text ;

    }
    // constructeur pour ajouer les reponses
    public Comments (int post_id , int user_id , String comment_text , int parent_id ) {
        this.post_id = post_id ;
        this.user_id = user_id ;
        this.comment_text = comment_text ;
        this.parent_id = parent_id ;
    }

    //getters
    public int getParent_id(){return parent_id ;}
    public int getComment_id() {return comment_id ;}
    public int getPost_id() {return post_id ;}
    public int getUser_id() {return user_id ;}
    public String getComment_text() {return comment_text ;}
    public String getDate() {return date ;}

    //setters
    public void setParent_id(int parent_id) {this.parent_id = parent_id ;}
    public void  setComment_id(int comment_id) {this.comment_id =comment_id;}
    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
    public void setUser_id(int user_id) {this.user_id = user_id;}
    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }
    public void setDate(String date) {
        this.date = date;
    }


}
