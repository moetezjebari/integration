package models;

import java.util.Date;

public class Reclamation {
    private int id;   // Identifiant simple
    private User user;   // Clé étrangère vers User
    private String description;
    private Mission mission; // Clé étrangère vers Mission
    private String status;
    private Date date;
    private String titre;

    // Constructeur par défaut
    public Reclamation() {}

    // Constructeur sans ID
    public Reclamation(User user, String description, Mission mission, String status, Date date, String titre) {
        this.user = user;
        this.description = description;
        this.mission = mission;
        this.status = status;
        this.date = date;
        this.titre = titre;
    }

    // Constructeur avec ID
    public Reclamation(int id, User user, String description, Mission mission, String status, Date date, String titre) {
        this.id = id;
        this.user = user;
        this.description = description;
        this.mission = mission;
        this.status = status;
        this.date = date;
        this.titre = titre;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", user=" + (user != null ? user.getNom() : "Non défini") +  // Utilisation de getNom() pour obtenir le nom
                ", description='" + description + '\'' +
                ", mission=" + (mission != null ? mission.getTitre() : "Non défini") +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", titre='" + titre + '\'' +
                '}';
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
}
