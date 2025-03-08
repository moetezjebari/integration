package models;

import java.util.Date;

public class Reclamation {
    private int id;
    private User user;
    private String description;
    private Mission mission;
    private String status;
    private Date date;
    private String titre;
    private int nombreReponses;

    // Constructeur par défaut
    public Reclamation() {}

    // Constructeur sans ID
    public Reclamation(User user, String description, Mission mission, String status, Date date, String titre, int nombreReponses) {
        this.user = user;
        this.description = description;
        this.mission = mission;
        this.status = status;
        this.date = date;
        this.titre = titre;
        this.nombreReponses = nombreReponses;
    }

    // Constructeur avec ID
    public Reclamation(int id, User user, String description, Mission mission, String status, Date date, String titre, int nombreReponses) {
        this.id = id;
        this.user = user;
        this.description = description;
        this.mission = mission;
        this.status = status;
        this.date = date;
        this.titre = titre;
        this.nombreReponses = nombreReponses;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", user=" + (user != null ? user.getNom() : "Non défini") +  // Utilisation de getNom() pour obtenir le nom de l'utilisateur
                ", description='" + description + '\'' +
                ", mission=" + (mission != null ? mission.getTitre() : "Non défini") +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", titre='" + titre + '\'' +
                ", nombreReponses=" + nombreReponses +
                '}';
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {  // Utiliser Utilisateur
        return user;
    }

    public void setUser(User user) {  // Utiliser Utilisateur
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getNombreReponses() {
        return nombreReponses;
    }

    public void setNombreReponses(int nombreReponses) {
        this.nombreReponses = nombreReponses;
    }
}
