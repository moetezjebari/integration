package services;

import models.Avis;
import models.Utilisateur; // Changement de User à Utilisateur
import tools.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AvisService {
    private Connection cnx;

    public AvisService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    public void ajouter(Avis avis) throws SQLException {
        if (avis.getUtilisateur() == null) {
            throw new SQLException("L'utilisateur de l'avis ne peut pas être nul.");
        }

        String sql = "INSERT INTO avis (utilisateur_id, note, commentaire, date_avis, type_avis) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, avis.getUtilisateur().getId());  // Make sure this is not null
        st.setInt(2, avis.getNote());
        st.setString(3, avis.getCommentaire());
        st.setTimestamp(4, Timestamp.valueOf(avis.getDateAvis())); // Conversion LocalDateTime → Timestamp
        st.setString(5, avis.getTypeAvis());
        st.executeUpdate();
        System.out.println("Avis ajouté avec succès !");
    }


    public void supprimer(Avis avis) throws SQLException {
        String sql = "DELETE FROM avis WHERE id = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, avis.getId());
        st.executeUpdate();
        System.out.println("Avis supprimé avec succès !");
    }

    public void modifier(Avis avis) throws SQLException {
        if (cnx == null || cnx.isClosed()) {
            throw new SQLException("La connexion à la base de données est fermée.");
        }

        // Désactiver l'auto-commit pour contrôler la transaction manuellement
        cnx.setAutoCommit(false);

        String checkQuery = "SELECT id FROM avis WHERE id = ?";
        PreparedStatement checkPs = cnx.prepareStatement(checkQuery);
        checkPs.setInt(1, avis.getId());
        ResultSet rs = checkPs.executeQuery();

        if (rs.next()) {
            // L'avis existe, on peut procéder à la mise à jour
            String updateQuery = "UPDATE avis SET note = ?, commentaire = ?, type_avis = ? WHERE id = ?";
            PreparedStatement ps = cnx.prepareStatement(updateQuery);
            ps.setInt(1, avis.getNote());
            ps.setString(2, avis.getCommentaire());
            ps.setString(3, avis.getTypeAvis());
            ps.setInt(4, avis.getId());
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                // Valider la transaction si la mise à jour a réussi
                cnx.commit();
                System.out.println("Avis mis à jour avec succès !");
            } else {
                System.out.println("Aucun avis trouvé avec cet ID.");
            }
        } else {
            System.out.println("Avis avec cet ID non trouvé.");
        }

        // Rétablir l'auto-commit à true après la transaction
        cnx.setAutoCommit(true);
    }

    public List<Avis> recuperer() throws SQLException {
        String sql = "SELECT * FROM avis";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Avis> avisList = new ArrayList<>();
        while (rs.next()) {
            Avis avis = new Avis();
            avis.setId(rs.getInt("id"));
            Utilisateur utilisateur = new Utilisateur(); // Changement de User à Utilisateur
            utilisateur.setId(rs.getInt("utilisateur_id")); // Remplissage de l'ID de l'utilisateur
            // Optionnellement, si vous avez d'autres informations sur l'utilisateur, vous pouvez les ajouter ici
            avis.setUtilisateur(utilisateur); // Assignation de l'utilisateur à l'avis
            avis.setNote(rs.getInt("note"));
            avis.setCommentaire(rs.getString("commentaire"));
            avis.setDateAvis(rs.getTimestamp("date_avis").toLocalDateTime()); // Conversion Timestamp → LocalDateTime
            avis.setTypeAvis(rs.getString("type_avis"));
            avisList.add(avis);
        }
        return avisList;
    }

    // Ajout de la méthode listerTousAvis()
    public List<Avis> listerTousAvis() throws SQLException {
        return recuperer(); // Appelle la méthode recuperer() pour obtenir tous les avis
    }

    // Méthode pour filtrer les avis par type
    public List<Avis> filtrerAvisParType(String typeAvis) throws SQLException {
        return recuperer().stream()
                .filter(avis -> avis.getTypeAvis().equalsIgnoreCase(typeAvis))
                .collect(Collectors.toList());
    }
}
