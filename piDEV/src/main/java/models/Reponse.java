package models;

import java.util.Date;

public class Reponse {
    private int id;
    private int reclamationId;
    private int utilisateurId;
    private String contenu;
    private String imagePath; // 🔹 Nouveau champ pour stocker le chemin de l'image
    private Date dateReponse;

    public Reponse() {
    }

    public Reponse(int reclamationId, int utilisateurId, String contenu, String imagePath, Date dateReponse) {
        this.reclamationId = reclamationId;
        this.utilisateurId = utilisateurId;
        this.contenu = contenu;
        this.imagePath = imagePath;
        this.dateReponse = dateReponse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReclamationId() {
        return reclamationId;
    }

    public void setReclamationId(int reclamationId) {
        this.reclamationId = reclamationId;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(Date dateReponse) {
        this.dateReponse = dateReponse;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", reclamationId=" + reclamationId +
                ", utilisateurId=" + utilisateurId +
                ", contenu='" + contenu + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", dateReponse=" + dateReponse +
                '}';
    }
}
