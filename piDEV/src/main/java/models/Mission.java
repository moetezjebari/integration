package models;

import java.util.Date;

public class Mission {
    private int id;
    private String titre;
    private String description;
    private int budget;
    private Date datePub;

    public Mission() {
    }

    public Mission(String titre, String description, int budget, Date datePub) {
        this.titre = titre;
        this.description = description;
        this.budget = budget;
        this.datePub = datePub;
    }

    public Mission(int id, String titre, String description, int budget, Date datePub) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.budget = budget;
        this.datePub = datePub;
    }

    // Ajout du constructeur avec un seul paramètre 'id'
    public Mission(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", budget=" + budget +
                ", datePub=" + datePub +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public Date getDatePub() {
        return datePub;
    }

    public void setDatePub(Date datePub) {
        this.datePub = datePub;
    }
}
