package models;

public class Candidature {
    private int id;
    private Utilisateur utilisateur;
    private Mission mission;
    private String image;
    private String reponseQuestions;
    private String lettreMotivation; // Nouvel attribut

    // Constructeurs
    public Candidature() {
    }

    public Candidature(int id, Utilisateur utilisateur, Mission mission, String image, String reponseQuestions, String lettreMotivation) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.mission = mission;
        this.image = image;
        this.reponseQuestions = reponseQuestions;
        this.lettreMotivation = lettreMotivation;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.utilisateur.setIdTo1();
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReponseQuestions() {
        return reponseQuestions;
    }

    public void setReponseQuestions(String reponseQuestions) {
        this.reponseQuestions = reponseQuestions;
    }

    public String getLettreMotivation() {
        return lettreMotivation;
    }

    public void setLettreMotivation(String lettreMotivation) {
        this.lettreMotivation = lettreMotivation;
    }

    // Méthode toString pour l'affichage
    @Override
    public String toString() {
        return "Candidature{" +
                "id=" + id +
                ", utilisateur=" + utilisateur +
                ", mission=" + mission +
                ", image='" + image + '\'' +
                ", reponseQuestions='" + reponseQuestions + '\'' +
                ", lettreMotivation='" + lettreMotivation + '\'' +
                '}';
    }
}