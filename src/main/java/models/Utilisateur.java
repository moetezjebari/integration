package models;

import java.sql.Timestamp;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

public class Utilisateur {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String typeUtilisateur;
    private String telephone;
    private Timestamp dateInscription;
    public void setIdTo1() {
        this.id = 1; // Set static ID to 1
    }
    // Default constructor
    public Utilisateur() {}

    // Constructor with ID and Timestamp (for database)
    public Utilisateur(int id, String nom, String prenom, String email, String typeUtilisateur, String telephone, Timestamp dateInscription) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.typeUtilisateur = typeUtilisateur;
        this.telephone = telephone;
        this.dateInscription = dateInscription;
    }

    // Constructor with ID but without Timestamp
    public Utilisateur(int id, String nom, String prenom, String email, String typeUtilisateur, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.typeUtilisateur = typeUtilisateur;
        this.telephone = telephone;
    }

    // Constructor for new user (no ID, with password and automatic registration date)
    public Utilisateur(String nom, String prenom, String email, String motDePasse, String telephone, String typeUtilisateur) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
        this.typeUtilisateur = typeUtilisateur;
        this.dateInscription = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = BCrypt.hashpw(motDePasse, BCrypt.gensalt()); // Hash password before storing
    }

    public boolean verifierMotDePasse(String motDePasse) {
        return BCrypt.checkpw(motDePasse, this.motDePasse); // Verify password against the hash
    }

    public String getTypeUtilisateur() {
        return typeUtilisateur;
    }

    public void setTypeUtilisateur(String typeUtilisateur) {
        this.typeUtilisateur = typeUtilisateur;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Timestamp getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Timestamp dateInscription) {
        this.dateInscription = dateInscription;
    }

    // Email validation (simple regex)
    public boolean isEmailValid() {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return this.email != null && this.email.matches(emailRegex);
    }

    // Phone number validation (simple 10-digit validation)
    public boolean isPhoneValid() {
        String phoneRegex = "^[0-9]{10}$"; // Assuming a 10-digit phone number
        return this.telephone != null && this.telephone.matches(phoneRegex);
    }

    // Override equals and hashCode methods for comparing objects and using in collections
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return id == that.id &&
                nom.equals(that.nom) &&
                prenom.equals(that.prenom) &&
                email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, email);
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", typeUtilisateur='" + typeUtilisateur + '\'' +
                ", dateInscription=" + dateInscription +
                '}';
    }
}
