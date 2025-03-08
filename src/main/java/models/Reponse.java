package models;

import java.util.Date;

public class Reponse {
    private int id;
    private Reclamation reclamation;
    private User user;  // ✅ Changed from Utilisateur to User
    private String contenu;
    private byte[] imageData;
    private Date dateCreation;

    public Reponse() {}

    public Reponse(Reclamation reclamation, User user, String contenu, byte[] imageData, Date dateCreation) {
        this.reclamation = reclamation;
        this.user = user;  // ✅ Now using User
        this.contenu = contenu;
        this.imageData = imageData;
        this.dateCreation = dateCreation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reclamation getReclamation() {
        return reclamation;
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }

    public User getUser() {  // ✅ Updated getter method
        return user;
    }

    public void setUser(User user) {  // ✅ Updated setter method
        this.user = user;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}
