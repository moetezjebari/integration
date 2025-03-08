package org.example.Controller;

import org.example.Model.Post;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;


public class PublicationController {
    @FXML
    private TableView<Post> tableView;
    @FXML
    private TableColumn<Post, Integer> colId;
    @FXML
    private TableColumn<Post, Integer> colUserId;
    @FXML
    private TableColumn<Post, String> colType;
    @FXML
    private TableColumn<Post, String> colContenu;
    @FXML
    private TableColumn<Post, String> colDate;



    private ObservableList<Post> publicationList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colContenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDate.setCellValueFactory(new PropertyValueFactory<>(""));

        // Charger les données dans la table
        tableView.setItems(getData());

    }

    // Fonction pour créer une liste de données
    private ObservableList<Post> getData() {
        return FXCollections.observableArrayList(
                new Post(1, 101, "Type A", "Contenu 1", "formation" , "contenu" , "2025-02-17"),
                new Post(2, 102, "Type B", "Contenu 2", "formation" , "contenu" , "2025-02-17"),
                new Post(3, 103, "Type C", "Contenu 3", "formation" , "contenu" , "2025-02-17")
        );
    }


}


 /*   private void loadPublications() {
        publicationList.clear();
        String query = "SELECT * FROM publication";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                publicationList.add(new Publication(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getString("image"),
                        rs.getTimestamp("date_publication"),
                        rs.getInt("auteur_id"),
                        rs.getString("statut"),
                        rs.getInt("categorie_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        publicationTable.setItems(publicationList);
    }

    @FXML
    private void handleNewPublication() {
        String query = "INSERT INTO publication (titre, contenu, image, auteur_id, statut, categorie_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, titreField.getText());
            stmt.setString(2, contenuField.getText());
            stmt.setString(3, imageField.getText());
            stmt.setInt(4, Integer.parseInt(auteurIdField.getText()));
            stmt.setString(5, statutField.getText());
            stmt.setInt(6, Integer.parseInt(categorieIdField.getText()));
            stmt.executeUpdate();
            loadPublications();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditPublication() {
        Publication selectedPublication = publicationTable.getSelectionModel().getSelectedItem();
        if (selectedPublication != null) {
            String query = "UPDATE publication SET titre = ?, contenu = ?, image = ?, statut = ?, categorie_id = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, titreField.getText());
                stmt.setString(2, contenuField.getText());
                stmt.setString(3, imageField.getText());
                stmt.setString(4, statutField.getText());
                stmt.setInt(5, Integer.parseInt(categorieIdField.getText()));
                stmt.setInt(6, selectedPublication.getId());
                stmt.executeUpdate();
                loadPublications();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeletePublication() {
        Publication selectedPublication = publicationTable.getSelectionModel().getSelectedItem();
        if (selectedPublication != null) {
            String query = "DELETE FROM publication WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, selectedPublication.getId());
                stmt.executeUpdate();
                loadPublications();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
*/