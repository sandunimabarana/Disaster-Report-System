/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drsystem.Controller;

import com.mycompany.drsystem.DatabaseConnection;
import com.mycompany.drsystem.Model.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 *
 * @author powertec
 */
public class DeleteAccountController {

    @FXML
    private ComboBox<User> userComboBox;

    @FXML
    private void initialize() {
        loadUsers();
    }

private void loadUsers() {
    ObservableList<User> users = FXCollections.observableArrayList();
    try (Connection connection = DatabaseConnection.getConnection(); 
         PreparedStatement statement = connection.prepareStatement("SELECT id, username, clientName, userType FROM User")) {

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String username = resultSet.getString("username");
            String clientName = resultSet.getString("clientName");
            String userType = resultSet.getString("userType");

            users.add(new User(id, username, clientName, userType));
        }
        userComboBox.setItems(users);
    } catch (SQLException e) {
        showAlert("Error", "Failed to load users: " + e.getMessage());
    }
}

    @FXML
    private void deleteAccount() {
        User selectedUser = userComboBox.getValue();

        if (selectedUser == null) {
            showAlert("Validation Error", "Please select a user to delete.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM User WHERE id = ?")) {

            statement.setInt(1, selectedUser.getId());
            statement.executeUpdate();
            showAlert("Success", "User account deleted successfully.");

            // Refresh the user list
            loadUsers();
        } catch (SQLException e) {
            showAlert("Error", "Failed to delete account: " + e.getMessage());
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            // Load the Admin Dashboard FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/drsystem/view/AdminPage.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene (Admin Dashboard)
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard"); // Optional: Set the window title
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Admin Dashboard.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
