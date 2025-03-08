package services;

import models.Reclamation;
import models.User;
import models.Mission;
import tools.DatabaseConnection;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.time.ZoneId;
import java.time.temporal.WeekFields;


import java.sql.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ReclamationService  {
    private Connection cnx;

    public ReclamationService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    public void ajouter(Reclamation r) throws SQLException {
        String sql = "INSERT INTO reclamation (user_Id, description, mission_Id, status, date, titre) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, r.getUser().getId());
            st.setString(2, r.getDescription());
            st.setInt(3, r.getMission().getId());
            st.setString(4, r.getStatus());
            st.setDate(5, new java.sql.Date(r.getDate().getTime()));
            st.setString(6, r.getTitre());
            st.executeUpdate();
            System.out.println("Réclamation ajoutée");
        }
    }

    public void supprimer(Reclamation r) throws SQLException {
        String sql = "DELETE FROM reclamation WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, r.getId());
            st.executeUpdate();
            System.out.println("Réclamation supprimée");
        }
    }

    public void modifier(Reclamation reclamation) throws SQLException {
        String sql = "UPDATE reclamation SET status = ?, description = ?, titre = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, reclamation.getStatus());
            ps.setString(2, reclamation.getDescription());
            ps.setString(3, reclamation.getTitre());
            ps.setInt(4, reclamation.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Réclamation mise à jour avec succès !");
            } else {
                System.out.println("Aucune réclamation trouvée avec cet ID.");
            }
        }
    }

    public List<Reclamation> recuperer() throws SQLException {
        String sql = "SELECT * FROM reclamation";
        List<Reclamation> reclamations = new ArrayList<>();

        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Reclamation r = new Reclamation();
                r.setId(rs.getInt("id"));

                int userId = rs.getInt("user_Id");
                int missionId = rs.getInt("mission_Id");

                r.setUser(recupererUtilisateurParId(userId));
                r.setMission(recupererMissionParId(missionId));
                r.setDescription(rs.getString("description"));
                r.setStatus(rs.getString("status"));
                r.setDate(rs.getDate("date"));
                r.setTitre(rs.getString("titre"));
                reclamations.add(r);
            }
        }
        return reclamations;
    }

    public List<Reclamation> rechercherParTitre(String titreRecherche) throws SQLException {
        return recuperer().stream()
                .filter(reclamation -> reclamation.getTitre().toLowerCase().startsWith(titreRecherche.toLowerCase()))
                .collect(Collectors.toList());
    }


    public User recupererUtilisateurParId(int userId) throws SQLException {
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("motDePasse"),
                            rs.getString("typeUtilisateur")
                    );
                }
            }
        }
        return null;
    }

    public Mission recupererMissionParId(int missionId) throws SQLException {
        String query = "SELECT * FROM mission WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, missionId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Date dateSql = rs.getDate("datePub"); // ✅ Get SQL Date
                    LocalDate localDate = dateSql != null ? dateSql.toLocalDate() : null; // ✅ Convert to LocalDate

                    return new Mission(
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("description"),
                            rs.getInt("duree"),
                            localDate
                    );
                }
            }
        }
        return null;
    }

    public int compterReponsesParReclamation(int reclamationId) throws SQLException {
        String query = "SELECT COUNT(*) FROM reponses WHERE reclamation_id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, reclamationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    public List<Reclamation> recupererTrieParReponses() throws SQLException {
        String sql = "SELECT r.*, (SELECT COUNT(*) FROM reponses WHERE reponses.reclamation_id = r.id) AS nombre_reponses " +
                "FROM reclamation r ORDER BY nombre_reponses DESC";

        try (PreparedStatement stmt = cnx.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Créer un Stream à partir du ResultSet
            return Stream.generate(() -> {
                        try {
                            if (rs.next()) {
                                Reclamation reclamation = new Reclamation();
                                reclamation.setId(rs.getInt("id"));
                                reclamation.setTitre(rs.getString("titre"));
                                reclamation.setDescription(rs.getString("description"));
                                reclamation.setStatus(rs.getString("status"));
                                reclamation.setDate(rs.getDate("date"));
                                reclamation.setNombreReponses(rs.getInt("nombre_reponses"));
                                return reclamation;
                            }
                            return null; // Fin du ResultSet
                        } catch (SQLException e) {
                            throw new RuntimeException("Erreur lors de la lecture des données de la réclamation", e);
                        }
                    })
                    .takeWhile(Objects::nonNull) // Arrêter le stream lorsque null est renvoyé
                    .collect(Collectors.toList()); // Collecter les résultats dans une liste
        }
    }







}
