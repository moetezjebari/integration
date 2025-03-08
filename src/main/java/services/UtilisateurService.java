package services;

import models.TypeUtilisateur;
import models.Utilisateur;
import tools.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 🔥 Service pour gérer les opérations CRUD sur les utilisateurs.
 */
public class UtilisateurService {

    private Connection conn;

    /**
     * 📌 Constructeur qui initialise la connexion à la base de données.
     */
    public UtilisateurService() {
        conn = DatabaseConnection.getInstance().getCnx();
    }

    /**
     * 📌 Vérifie si l'email est valide.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    /**
     * 📌 Vérifie si un champ donné n'est pas vide.
     */
    private boolean isNotEmpty(String field) {
        return field != null && !field.trim().isEmpty();
    }

    /**
     * 📌 Ajoute un nouvel utilisateur après validation des entrées.
     */
    public boolean ajouterUtilisateur(Utilisateur user) {
        if (!isNotEmpty(user.getNom()) || !isNotEmpty(user.getPrenom()) || !isNotEmpty(user.getEmail()) ||
                !isNotEmpty(user.getMotDePasse()) || !isNotEmpty(user.getTypeUtilisateur()) || !isNotEmpty(user.getTelephone())) {
            System.out.println("Erreur : Tous les champs doivent être remplis.");
            return false;
        }

        if (!isValidEmail(user.getEmail())) {
            System.out.println("Erreur : Email invalide.");
            return false;
        }

        String query = "INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, type_utilisateur, telephone, date_inscription) VALUES (?, ?, ?, SHA2(?, 256), ?, ?, NOW())";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getNom());
            pstmt.setString(2, user.getPrenom());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getMotDePasse());
            pstmt.setString(5, user.getTypeUtilisateur());
            pstmt.setString(6, user.getTelephone());  // Ajout du téléphone

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 📌 Récupère tous les utilisateurs de la base de données.
     */
    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT id, nom, prenom, email, type_utilisateur, telephone, date_inscription FROM utilisateur";  // Ajout du téléphone à la requête

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Utilisateur user = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("type_utilisateur"),
                        rs.getString("telephone"),  // Récupération du téléphone
                        rs.getTimestamp("date_inscription") // ✅ Récupérer la date d'inscription
                );
                utilisateurs.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }

    /**
     * 📌 Vérifie si un utilisateur existe avant de le supprimer.
     */
    public boolean utilisateurExiste(int id) {
        String query = "SELECT id FROM utilisateur WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 📌 Supprime un utilisateur par son ID après vérification de son existence.
     */
    public boolean supprimerUtilisateur(int id) {
        if (!utilisateurExiste(id)) {
            System.out.println("Erreur : L'utilisateur avec ID " + id + " n'existe pas.");
            return false;
        }

        String query = "DELETE FROM utilisateur WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 📌 Met à jour un utilisateur existant avec validation des entrées.
     */
    public boolean mettreAJourUtilisateur(Utilisateur user) {
        if (!isNotEmpty(user.getNom()) || !isNotEmpty(user.getPrenom()) || !isNotEmpty(user.getEmail()) || !isNotEmpty(user.getTypeUtilisateur()) || !isNotEmpty(user.getTelephone())) {
            System.out.println("Erreur : Tous les champs doivent être remplis.");
            return false;
        }

        if (!isValidEmail(user.getEmail())) {
            System.out.println("Erreur : Email invalide.");
            return false;
        }

        String query = "UPDATE utilisateur SET nom=?, prenom=?, email=?, type_utilisateur=?, telephone=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getNom());
            pstmt.setString(2, user.getPrenom());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getTypeUtilisateur());
            pstmt.setString(5, user.getTelephone());  // Mise à jour du téléphone
            pstmt.setInt(6, user.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Utilisateur> getAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateur";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getString("type_utilisateur")
                );
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des utilisateurs.");
            e.printStackTrace();
        }
        return utilisateurs;
    }
    public Utilisateur getById(int id) {
        String query = "SELECT * FROM utilisateur WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getString("type_utilisateur")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération de l'utilisateur.");
            e.printStackTrace();
        }
        return null;
    }

}
