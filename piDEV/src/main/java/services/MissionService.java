package services;

import models.Mission;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MissionService implements IService<Mission> {
    private Connection cnx;

    public MissionService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Mission m) throws SQLException {
        String sql = "INSERT INTO mission (titre, description, budget, date_pub) VALUES (?, ?, ?, ?)";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, m.getTitre());
        st.setString(2, m.getDescription());
        st.setInt(3, m.getBudget());
        st.setDate(4, new java.sql.Date(m.getDatePub().getTime())); // Conversion java.util.Date → java.sql.Date
        st.executeUpdate();
        System.out.println("Mission ajoutée");
    }

    @Override
    public void supprimer(Mission m) throws SQLException {
        String sql = "DELETE FROM mission WHERE id = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, m.getId());
        st.executeUpdate();
        System.out.println("Mission supprimée");
    }

    @Override
    public void modifier(Mission mission) throws SQLException {
        // Vérifier que la connexion est bien ouverte
        if (cnx == null || cnx.isClosed()) {
            throw new SQLException("La connexion à la base de données est fermée.");
        }

        // Désactiver l'auto-commit pour contrôler la transaction manuellement
        cnx.setAutoCommit(false);

        String checkQuery = "SELECT id FROM mission WHERE id = ?";
        PreparedStatement checkPs = cnx.prepareStatement(checkQuery);
        checkPs.setInt(1, mission.getId());
        ResultSet rs = checkPs.executeQuery();

        // Vérifier si la mission existe dans la base de données
        if (rs.next()) {
            // La mission existe, on peut procéder à la mise à jour
            String updateQuery = "UPDATE mission SET titre = ?, description = ?, budget = ?, date_pub = ? WHERE id = ?";
            PreparedStatement ps = cnx.prepareStatement(updateQuery);
            ps.setString(1, mission.getTitre());
            ps.setString(2, mission.getDescription());
            ps.setInt(3, mission.getBudget());
            ps.setDate(4, new java.sql.Date(mission.getDatePub().getTime())); // Conversion java.util.Date → java.sql.Date
            ps.setInt(5, mission.getId());
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                // Valider la transaction si la mise à jour a réussi
                cnx.commit();
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
        cnx.setAutoCommit(true);
    }

    @Override
    public List<Mission> recuperer() throws SQLException {
        String sql = "SELECT * FROM mission";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Mission> missions = new ArrayList<>();
        while (rs.next()) {
            Mission m = new Mission();
            m.setId(rs.getInt("id"));
            m.setTitre(rs.getString("titre"));
            m.setDescription(rs.getString("description"));
            m.setBudget(rs.getInt("budget"));
            m.setDatePub(rs.getDate("date_pub"));
            missions.add(m);
        }
        return missions;
    }
}
