package services;

import models.Candidature;
import models.Mission;
import models.Utilisateur;
import tools.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CandidatureService implements ICandidature {
    private Connection connection;

    public CandidatureService() {
        connection = DatabaseConnection.getInstance().getCnx();
    }

    // Méthode pour compter les candidatures pour une mission donnée
    public int compterCandidaturesParMission(int missionId) {
        String query = "SELECT COUNT(*) AS nombreCandidatures FROM candidature WHERE missionId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, missionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("nombreCandidatures");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors du comptage des candidatures.");
            e.printStackTrace();
        }
        return 0; // Retourne 0 en cas d'erreur ou si aucune candidature n'est trouvée
    }

    @Override
    public boolean add(Candidature candidature) {
        // Validation de la mission et de l'utilisateur
        MissionService missionService = new MissionService();
        Mission mission = missionService.getById(candidature.getMission().getId());
        if (mission == null) {
            System.err.println("❌ Erreur : Mission avec ID " + candidature.getMission().getId() + " n'existe pas.");
            return false;
        }

        UtilisateurService utilisateurService = new UtilisateurService();
        Utilisateur utilisateur = utilisateurService.getById(candidature.getUtilisateur().getId());
        if (utilisateur == null) {
            System.err.println("❌ Erreur : Utilisateur avec ID " + candidature.getUtilisateur().getId() + " n'existe pas.");
            return false;
        }

        // Insertion de la candidature
        String query = "INSERT INTO candidature (userId, missionId, image, reponseQuestions, lettreMotivation) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, candidature.getUtilisateur().getId());
            stmt.setInt(2, candidature.getMission().getId());
            stmt.setString(3, candidature.getImage());
            stmt.setString(4, candidature.getReponseQuestions());
            stmt.setString(5, candidature.getLettreMotivation());
            stmt.executeUpdate();
            System.out.println("✅ Candidature ajoutée avec succès.");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de la candidature.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(Candidature candidature) {
        String query = "UPDATE candidature SET userId = ?, missionId = ?, image = ?, reponseQuestions = ?, lettreMotivation = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, candidature.getUtilisateur().getId());
            stmt.setInt(2, candidature.getMission().getId());
            stmt.setString(3, candidature.getImage());
            stmt.setString(4, candidature.getReponseQuestions());
            stmt.setString(5, candidature.getLettreMotivation());
            stmt.setInt(6, candidature.getId());
            stmt.executeUpdate();
            System.out.println("✅ Candidature mise à jour avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour de la candidature.");
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM candidature WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Candidature supprimée avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de la candidature.");
            e.printStackTrace();
        }
    }

    @Override
    public Candidature getById(int id) {
        String query = "SELECT * FROM candidature WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur utilisateur = new UtilisateurService().getById(rs.getInt("userId"));
                Mission mission = new MissionService().getById(rs.getInt("missionId"));

                return new Candidature(
                        rs.getInt("id"),
                        utilisateur,
                        mission,
                        rs.getString("image"),
                        rs.getString("reponseQuestions"),
                        rs.getString("lettreMotivation")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération de la candidature.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Candidature> getAll() {
        List<Candidature> candidatures = new ArrayList<>();
        String query = "SELECT * FROM candidature";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Utilisateur utilisateur = new UtilisateurService().getById(rs.getInt("userId"));
                Mission mission = new MissionService().getById(rs.getInt("missionId"));

                Candidature candidature = new Candidature(
                        rs.getInt("id"),
                        utilisateur,
                        mission,
                        rs.getString("image"),
                        rs.getString("reponseQuestions"),
                        rs.getString("lettreMotivation")
                );
                candidatures.add(candidature);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des candidatures.");
            e.printStackTrace();
        }
        return candidatures;
    }
}