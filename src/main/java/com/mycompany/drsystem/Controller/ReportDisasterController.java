/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drsystem.Controller;

import com.mycompany.drsystem.*;
import com.mycompany.drsystem.Model.Disaster;
import com.mycompany.drsystem.Model.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author powertec
 */
public class ReportDisasterController {

    @FXML
    private ComboBox<String> disaster_type_id;

    @FXML
    private TextField location_id;

    @FXML
    private TextArea description_id;

    @FXML
    private Button submit_btn_id;

    @FXML
    private Button back_btn_id;

    @FXML
    private Button logout_btn_id;

    private User currentUser; // Assume you have a way to set the current user

    @FXML
    public void initialize() {
        // Initialize ComboBox with several types of disasters
        disaster_type_id.getItems().addAll(
                "Flood",
                "Earthquake",
                "Fire",
                "Hurricane",
                "Tornado",
                "Volcano Eruption",
                "Landslide",
                "Drought",
                "Tsunami",
                "Chemical Spill",
                "Pandemic"
        );
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        String disasterType = disaster_type_id.getValue();
        String location = location_id.getText();
        String description = description_id.getText();

        int userId = currentUser.getId(); // Get the actual user ID
        
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Convert LocalDateTime to Timestamp
        Timestamp currentTimestamp = Timestamp.valueOf(currentDateTime);

        Disaster disaster = new Disaster(0, disasterType, location, description, userId, currentTimestamp, "New");

        try (Connection connection = DatabaseConnection.getConnection()) {
            String insertQuery = "INSERT INTO Disaster (DisasterType, Location, Description, UserId) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, disaster.getDisasterType());
            preparedStatement.setString(2, disaster.getLocation());
            preparedStatement.setString(3, disaster.getDescription());
            preparedStatement.setInt(4, disaster.getUserId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Disaster reported successfully!");

                // Clear the input fields after successful submission
                disaster_type_id.setValue(null);
                location_id.clear();
                description_id.clear();
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to report disaster. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "An error occurred while reporting the disaster.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/drsystem/View/UserPage.fxml"));
            AnchorPane userPage = loader.load();
            Scene userScene = new Scene(userPage);
            Stage currentStage = (Stage) back_btn_id.getScene().getWindow();
            currentStage.setScene(userScene);
            currentStage.setTitle("User Page"); // Set title for User page
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load user page.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/drsystem/View/Login.fxml"));
            AnchorPane loginPage = loader.load();
            Scene loginScene = new Scene(loginPage);
            Stage currentStage = (Stage) logout_btn_id.getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login Page"); // Set title for Login page
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load login page.");
        }
    }
}
