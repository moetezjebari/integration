package services;

import models.Reclamation;
import models.User;
import models.Mission;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements IService<Reclamation> {
    private Connection cnx;

    public ReclamationService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Reclamation r) throws SQLException {
        String sql = "INSERT INTO reclamation (userId, description, missionId, status, date, titre) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, r.getUser().getId());  // Utilisation de l'ID de l'objet User
            st.setString(2, r.getDescription());
            st.setInt(3, r.getMission().getId());  // Utilisation de l'ID de l'objet Mission
            st.setString(4, r.getStatus());
            st.setDate(5, new java.sql.Date(r.getDate().getTime())); // Conversion java.util.Date → java.sql.Date
            st.setString(6, r.getTitre());  // Ajout du titre dans l'insertion
            st.executeUpdate();
            System.out.println("Réclamation ajoutée");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
            throw e;  // Relancer l'exception pour la propagation
        }
    }


    @Override
    public void supprimer(Reclamation r) throws SQLException {
        String sql = "DELETE FROM reclamation WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, r.getId());
            st.executeUpdate();
            System.out.println("Réclamation supprimée");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réclamation : " + e.getMessage());
            throw e;  // Relancer l'exception pour la propagation
        }
    }

    @Override
    public void modifier(Reclamation reclamation) throws SQLException {
        if (cnx == null || cnx.isClosed()) {
            throw new SQLException("La connexion à la base de données est fermée.");
        }

        // Désactiver l'auto-commit pour contrôler la transaction manuellement
        cnx.setAutoCommit(false);

        String checkQuery = "SELECT id FROM reclamation WHERE id = ?";
        try (PreparedStatement checkPs = cnx.prepareStatement(checkQuery)) {
            checkPs.setInt(1, reclamation.getId());
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next()) {
                    // La réclamation existe, on peut procéder à la mise à jour
                    String updateQuery = "UPDATE reclamation SET status = ?, description = ?, titre = ? WHERE id = ?";
                    try (PreparedStatement ps = cnx.prepareStatement(updateQuery)) {
                        ps.setString(1, reclamation.getStatus());
                        ps.setString(2, reclamation.getDescription());
                        ps.setString(3, reclamation.getTitre());
                        ps.setInt(4, reclamation.getId());

                        int rowsUpdated = ps.executeUpdate();
                        if (rowsUpdated > 0) {
                            // Valider la transaction si la mise à jour a réussi
                            cnx.commit();
                            System.out.println("Réclamation mise à jour avec succès !");
                        } else {
                            System.out.println("Aucune réclamation trouvée avec cet ID.");
                        }
                    }
                } else {
                    System.out.println("Réclamation avec cet ID non trouvée.");
                }
            }
        } catch (SQLException e) {
            cnx.rollback();  // Rétablir l'auto-commit en cas d'erreur
            System.err.println("Erreur lors de la modification de la réclamation : " + e.getMessage());
            throw e;
        } finally {
            cnx.setAutoCommit(true);  // Rétablir l'auto-commit à true après la transaction
        }
    }

    @Override
    public List<Reclamation> recuperer() throws SQLException {
        String sql = "SELECT * FROM reclamation";
        List<Reclamation> reclamations = new ArrayList<>();

        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Reclamation r = new Reclamation();
                r.setId(rs.getInt("id"));

                // Récupérer l'objet User en utilisant son ID
                int userId = rs.getInt("userId");
                User user = getUserById(userId);  // Méthode pour récupérer un User par ID
                r.setUser(user);

                // Récupérer l'objet Mission en utilisant son ID
                int missionId = rs.getInt("missionId");
                Mission mission = getMissionById(missionId);  // Méthode pour récupérer une Mission par ID
                r.setMission(mission);

                r.setDescription(rs.getString("description"));
                r.setStatus(rs.getString("status"));
                r.setDate(rs.getDate("date"));
                r.setTitre(rs.getString("titre"));
                reclamations.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réclamations : " + e.getMessage());
            throw e;
        }

        return reclamations;
    }

    private User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setEmail(rs.getString("email"));
                    user.setMotDePasse(rs.getString("motDePasse"));
                    user.setTypeUtilisateur(rs.getString("typeUtilisateur"));
                    return user;
                }
            }
        }
        return null;  // Retourne null si l'utilisateur n'est pas trouvé
    }

    private Mission getMissionById(int missionId) throws SQLException {
        String sql = "SELECT * FROM mission WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, missionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Mission mission = new Mission();
                    mission.setId(rs.getInt("id"));
                    mission.setTitre(rs.getString("titre"));
                    return mission;
                }
            }
        }
        return null;  // Retourne null si la mission n'est pas trouvée
    }
}
