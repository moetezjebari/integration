package services;

import models.Reclamation;
import models.Reponse;
import models.User;
import models.Utilisateur; // Changement de User à Utilisateur
import tools.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService {
    private final Connection cnx;

    public ReponseService() {
        cnx = DatabaseConnection.getInstance().getCnx(); // Connexion JDBC
    }

    /**
     * Ajouter une réponse avec image en BLOB
     */
    public void ajouter(Reponse reponse) throws SQLException {
        String query = "INSERT INTO reponses (reclamation_id, user_id, contenu, image, date_creation) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reponse.getReclamation().getId());
            stmt.setInt(2, reponse.getUser().getId()); // Utilisation de Utilisateur
            stmt.setString(3, reponse.getContenu());
            stmt.setBytes(4, reponse.getImageData()); // Stocker l'image en BLOB
            stmt.setTimestamp(5, new Timestamp(reponse.getDateCreation().getTime()));
            stmt.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reponse.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    /**
     * Mettre à jour une réponse
     */
    public void modifier(Reponse reponse) throws SQLException {
        String query = "UPDATE reponses SET contenu = ?, image = ?, date_creation = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, reponse.getContenu());
            stmt.setBytes(2, reponse.getImageData());
            stmt.setTimestamp(3, new Timestamp(reponse.getDateCreation().getTime()));
            stmt.setInt(4, reponse.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Supprimer une réponse par ID
     */
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM reponses WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Récupérer une réponse par ID
     */
    public Reponse getById(int id) throws SQLException {
        String query = "SELECT * FROM reponses WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractReponseFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Récupérer toutes les réponses
     */
    public List<Reponse> getAll() throws SQLException {
        List<Reponse> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponses";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reponses.add(extractReponseFromResultSet(rs));
            }
        }
        return reponses;
    }

    /**
     * Récupérer les réponses d'une réclamation spécifique
     */
    public List<Reponse> recupererParReclamation(Reclamation reclamation) throws SQLException {
        List<Reponse> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponses WHERE reclamation_id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, reclamation.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reponses.add(extractReponseFromResultSet(rs));
                }
            }
        }
        return reponses;
    }

    /**
     * Extraire une réponse depuis un ResultSet
     */
    private Reponse extractReponseFromResultSet(ResultSet rs) throws SQLException {
        Reponse reponse = new Reponse();
        reponse.setId(rs.getInt("id"));
        reponse.setContenu(rs.getString("contenu"));
        reponse.setImageData(rs.getBytes("image")); // Charger l'image en BLOB
        reponse.setDateCreation(rs.getTimestamp("date_creation"));

        // Charger la réclamation associée
        Reclamation reclamation = new Reclamation();
        reclamation.setId(rs.getInt("reclamation_id"));
        reponse.setReclamation(reclamation);

        // Charger l'utilisateur associé
        User user = new User();
        user.setId(rs.getInt("user_id"));
        reponse.setUser(user);

        return reponse;
    }
}
