package services;

import models.Reponse;
import tools.MyDataBase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements IService<Reponse> {
    private Connection cnx;

    public ReponseService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Reponse r) throws SQLException {
        String sql = "INSERT INTO reponse (reclamation_id, utilisateur_id, contenu, image_path, date_reponse) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, r.getReclamationId());
        st.setInt(2, r.getUtilisateurId());
        st.setString(3, r.getContenu());
        st.setString(4, r.getImagePath()); // 🔹 Enregistrer le chemin de l'image
        st.setTimestamp(5, new Timestamp(r.getDateReponse().getTime()));
        st.executeUpdate();
        System.out.println("Réponse ajoutée avec succès !");
    }

    @Override
    public void supprimer(Reponse r) throws SQLException {
        String sql = "DELETE FROM reponse WHERE id = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, r.getId());
        st.executeUpdate();
        System.out.println("Réponse supprimée avec succès !");
    }

    @Override
    public void modifier(Reponse r) throws SQLException {
        // Requête SQL pour mettre à jour une réponse
        String sql = "UPDATE reponse SET contenu = ?, image_path = ? WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            // Lier les paramètres
            st.setString(1, r.getContenu()); // Nouvelle description
            st.setString(2, r.getImagePath()); // Nouveau chemin d'image
            st.setInt(3, r.getId()); // ID de la réponse
            st.executeUpdate();
        }
    }


    @Override
    public List<Reponse> recuperer() throws SQLException {
        String sql = "SELECT * FROM reponse";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Reponse> reponses = new ArrayList<>();
        while (rs.next()) {
            Reponse r = new Reponse();
            r.setId(rs.getInt("id"));
            r.setReclamationId(rs.getInt("reclamation_id"));
            r.setUtilisateurId(rs.getInt("utilisateur_id"));
            r.setContenu(rs.getString("contenu"));
            r.setImagePath(rs.getString("image_path"));
            r.setDateReponse(rs.getTimestamp("date_reponse"));
            reponses.add(r);
        }
        return reponses;
    }

    public String uploaderImage(File image) {
        String destinationPath = "uploads/" + image.getName();
        File destinationFile = new File(destinationPath);

        try {
            Files.copy(image.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ Image uploadée : " + destinationPath);
            return destinationPath;
        } catch (IOException e) {
            System.out.println("❌ Erreur lors de l'upload de l'image !");
            e.printStackTrace();
            return null;
        }
    }
}
