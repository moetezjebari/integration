package services;

import models.Mission;
import tools.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MissionService  {
    private final Connection connection;

    public MissionService() {
        connection = DatabaseConnection.getInstance().getCnx();
    }


    public void add(Mission mission) {
        String query = "INSERT INTO mission (titre, description, competance, duree, budget, datePub, questions, nomEntreprise, nombreCandidatures) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, mission.getTitre());
            stmt.setString(2, mission.getDescription());
            stmt.setString(3, mission.getCompetance());
            stmt.setInt(4, mission.getDuree());
            stmt.setDouble(5, mission.getBudget());
            stmt.setDate(6, Date.valueOf(mission.getDatePub()));
            stmt.setString(7, mission.getQuestions());
            stmt.setString(8, mission.getNomEntreprise());
            stmt.setInt(9, mission.getNombreCandidatures());
            stmt.executeUpdate();
            System.out.println("✅ Mission ajoutée avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de la mission.");
            e.printStackTrace();
        }
    }

    public boolean update(Mission mission) {
        String query = "UPDATE mission SET titre = ?, description = ?, competance = ?, duree = ?, budget = ?, datePub = ?, questions = ?, nomEntreprise = ?, nombreCandidatures = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, mission.getTitre());
            stmt.setString(2, mission.getDescription());
            stmt.setString(3, mission.getCompetance());
            stmt.setInt(4, mission.getDuree());
            stmt.setDouble(5, mission.getBudget());
            stmt.setDate(6, Date.valueOf(mission.getDatePub()));
            stmt.setString(7, mission.getQuestions());
            stmt.setString(8, mission.getNomEntreprise());
            stmt.setInt(9, mission.getNombreCandidatures());
            stmt.setInt(10, mission.getId());
            stmt.executeUpdate();
            System.out.println("✅ Mission mise à jour avec succès.");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour de la mission.");
            e.printStackTrace();
        }
        return false;
    }

    public void delete(int id) {
        String query = "DELETE FROM mission WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Mission supprimée avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de la mission.");
            e.printStackTrace();
        }
    }

    public Mission getById(int id) {
        String query = "SELECT * FROM mission WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Mission(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("competance"),
                        rs.getInt("duree"),
                        rs.getDouble("budget"),
                        rs.getDate("datePub").toLocalDate(),
                        rs.getString("questions"),
                        rs.getString("nomEntreprise"),
                        rs.getInt("nombreCandidatures")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération de la mission.");
            e.printStackTrace();
        }
        return null;
    }

    public List<Mission> getAll() {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM mission";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Mission mission = new Mission(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("competance"),
                        rs.getInt("duree"),
                        rs.getDouble("budget"),
                        rs.getDate("datePub").toLocalDate(),
                        rs.getString("questions"),
                        rs.getString("nomEntreprise"),
                        rs.getInt("nombreCandidatures")
                );
                missions.add(mission);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des missions.");
            e.printStackTrace();
        }
        return missions;
    }

    public void supprimer(int id) {
        delete(id);
    }
    public void ajouter(Mission m) throws SQLException {
        String sql = "INSERT INTO mission (titre, description, budget, date_pub) VALUES (?, ?, ?, ?)";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, m.getTitre());
        st.setString(2, m.getDescription());
        st.setDouble(3, m.getBudget()); // Corrected to setDouble for budget (double)
        st.setDate(4, new java.sql.Date(m.getDatePub().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())); // Convert LocalDate to java.sql.Date
        st.executeUpdate();
        System.out.println("Mission ajoutée");
    }

    public void supprimer(Mission m) throws SQLException {
        String sql = "DELETE FROM mission WHERE id = ?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, m.getId());
        st.executeUpdate();
        System.out.println("Mission supprimée");
    }

    public void modifier(Mission mission) throws SQLException {
        // Vérifier que la connexion est bien ouverte
        if (connection == null || connection.isClosed()) {
            throw new SQLException("La connexion à la base de données est fermée.");
        }

        // Désactiver l'auto-commit pour contrôler la transaction manuellement
        connection.setAutoCommit(false);

        String checkQuery = "SELECT id FROM mission WHERE id = ?";
        PreparedStatement checkPs = connection.prepareStatement(checkQuery);
        checkPs.setInt(1, mission.getId());
        ResultSet rs = checkPs.executeQuery();

        // Vérifier si la mission existe dans la base de données
        if (rs.next()) {
            // La mission existe, on peut procéder à la mise à jour
            String updateQuery = "UPDATE mission SET titre = ?, description = ?, budget = ?, date_pub = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(updateQuery);
            ps.setString(1, mission.getTitre());
            ps.setString(2, mission.getDescription());
            ps.setDouble(3, mission.getBudget()); // Corrected to setDouble for budget (double)
            ps.setDate(4, new java.sql.Date(mission.getDatePub().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())); // Convert LocalDate to java.sql.Date
            ps.setInt(5, mission.getId());
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                // Valider la transaction si la mise à jour a réussi
                connection.commit();
                System.out.println("Mission mise à jour avec succès !");
            } else {
                // Si aucune ligne n'a été mise à jour (probablement à cause de l'ID)
                System.out.println("Aucune mission trouvée avec cet ID.");
            }
        } else {
            // Si la mission n'existe pas
            System.out.println("Mission avec cet ID non trouvée.");
        }

        // Rétablir l'auto-commit à true après la transaction
        connection.setAutoCommit(true);
    }

    public List<Mission> recuperer() throws SQLException {
        String sql = "SELECT * FROM mission";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Mission> missions = new ArrayList<>();
        while (rs.next()) {
            Mission m = new Mission();
            m.setId(rs.getInt("id"));
            m.setTitre(rs.getString("titre"));
            m.setDescription(rs.getString("description"));
            m.setBudget(rs.getDouble("budget"));
            m.setDatePub(rs.getDate("date_pub").toLocalDate());
            missions.add(m);
        }
        return missions;
    }
    public List<Mission> getAllMissions() {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT id, titre, description, duree, datePub FROM mission"; // Ensure you select all necessary columns

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Date dateSql = rs.getDate("datePub"); // Get SQL Date
                LocalDate localDate = (dateSql != null) ? dateSql.toLocalDate() : null; // Convert to LocalDate

                Mission mission = new Mission(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getInt("duree"),
                        localDate
                );

                missions.add(mission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return missions;
    }

}
