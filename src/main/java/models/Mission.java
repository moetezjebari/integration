package models;
import java.util.Date;
import java.time.ZoneId;
import java.time.LocalDate;

public class Mission {
    private int id;
    private String titre;
    private String description;
    private String competance;
    private int duree;
    private double budget;
    private LocalDate datePub;
    private String questions;
    private String nomEntreprise;
    private int nombreCandidatures; // Nouveau champ

    // Constructeur par défaut
    public Mission() {}

    public Mission(int id, String titre, String description, int duree, Date datePub) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.duree = duree;
        this.datePub = (datePub != null) ? datePub.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }


    // Constructeur avec tous les attributs (sans ID et nombreCandidatures)
    public Mission(String titre, String description, String competance, int duree, double budget, LocalDate datePub, String questions, String nomEntreprise) {
        this.titre = titre;
        this.description = description;
        this.competance = competance;
        this.duree = duree;
        this.budget = budget;
        this.datePub = datePub;
        this.questions = questions;
        this.nomEntreprise = nomEntreprise;
        this.nombreCandidatures = 0; // Initialisé à 0 par défaut
    }

    // Constructeur avec tous les attributs (y compris ID et nombreCandidatures)
    public Mission(int id, String titre, String description, String competance, int duree, double budget, LocalDate datePub, String questions, String nomEntreprise, int nombreCandidatures) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.competance = competance;
        this.duree = duree;
        this.budget = budget;
        this.datePub = datePub;
        this.questions = questions;
        this.nomEntreprise = nomEntreprise;
        this.nombreCandidatures = nombreCandidatures;
    }

    public Mission(int missionId, String titre, String description, int duree, LocalDate localDate) {
        this.id = missionId;
        this.titre = titre;
        this.description = description;
        this.duree = duree;
        this.datePub = localDate;
    }


    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCompetance() { return competance; }
    public void setCompetance(String competance) { this.competance = competance; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public LocalDate getDatePub() { return datePub; }
    public void setDatePub(LocalDate datePub) { this.datePub = datePub; }

    public String getQuestions() { return questions; }
    public void setQuestions(String questions) { this.questions = questions; }

    public String getNomEntreprise() { return nomEntreprise; }
    public void setNomEntreprise(String nomEntreprise) { this.nomEntreprise = nomEntreprise; }

    public int getNombreCandidatures() { return nombreCandidatures; }
    public void setNombreCandidatures(int nombreCandidatures) { this.nombreCandidatures = nombreCandidatures; }

    @Override
    public String toString() {
        return "Mission{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", competance='" + competance + '\'' +
                ", duree=" + duree +
                ", budget=" + budget +
                ", datePub=" + datePub +
                ", questions='" + questions + '\'' +
                ", nomEntreprise='" + nomEntreprise + '\'' +
                ", nombreCandidatures=" + nombreCandidatures +
                '}';
    }

}