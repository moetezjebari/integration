package models;
import java.time.LocalDateTime;

public class Avis {
    private int id;
    private Utilisateur utilisateur; // Changed from User to Utilisateur
    private int note;
    private String commentaire;
    private LocalDateTime dateAvis;
    private String typeAvis; // "Freelance", "Client", "Service", etc.

    // Constructors
    public Avis() {}

    public Avis(int id, Utilisateur utilisateur, int note, String commentaire, LocalDateTime dateAvis, String typeAvis) {
        this.id = id;
        this.utilisateur = utilisateur; // Changed from user to utilisateur
        this.note = note;
        this.commentaire = commentaire;
        this.dateAvis = dateAvis;
        this.typeAvis = typeAvis;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Utilisateur getUtilisateur() { return utilisateur; } // Changed from getUser to getUtilisateur
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; } // Changed from setUser to setUtilisateur

    public int getNote() { return note; }
    public void setNote(int note) { this.note = note; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public LocalDateTime getDateAvis() { return dateAvis; }
    public void setDateAvis(LocalDateTime dateAvis) { this.dateAvis = dateAvis; }

    public String getTypeAvis() { return typeAvis; }
    public void setTypeAvis(String typeAvis) { this.typeAvis = typeAvis; }

    @Override
    public String toString() {
        return "Avis{" +
                "id=" + id +
                ", utilisateur=" + utilisateur + // Changed from user to utilisateur
                ", note=" + note +
                ", commentaire='" + commentaire + '\'' +
                ", dateAvis=" + dateAvis +
                ", typeAvis='" + typeAvis + '\'' +
                '}';
    }
}
