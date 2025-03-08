package org.example.Model;

public class Post {
    private int id;
    private int UserId;
    private String type;
    private String contenu;
    private String date;
    private String image ;
    private String title ;


    //constructeur pour recuperer un Post existant :
    public Post(int id , int UserId , String image, String title , String type , String contenu , String date )
    {
        this.id  = id ;
        this.UserId = UserId ;
        this.contenu = contenu ;
        this.image = image ;
        this.title = title ;
        this.type = type ;
        this.date = date ;

    }
    //constructeur pour ajouter une Post
    public Post(int UserId , String image, String title , String type , String contenu  )
    {
        this.UserId = UserId ;
        this.contenu = contenu ;
        this.image = image ;
        this.title = title ;
        this.type = type ;


    }

    //getters :

    public int getId() {
        return this.id;
    }
    public int getUserId() {
        return this.UserId;
    }
    public String getCategory(){
        return this.type  ;
    }
    public String getContent() {
        return this.contenu;
    }
    public String getDate() {
        return this.date;
    }
    public String getImage() { return this.image ;}
    public String getTitle() {return this.title ;}


    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setAuteurId(int auteurId) {
        this.UserId = auteurId;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setContent(String contenu) {
        this.contenu = contenu;
    }
    public void setDate(String datePublication) {
        this.date = datePublication;
    }
    public void setImage(String image ) { this.image = image ; }
    public void setTitle(String title) {this.title = title ; }



}
