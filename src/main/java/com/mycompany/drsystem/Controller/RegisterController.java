/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drsystem.Controller;

import com.mycompany.drsystem.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author powertec
 */
public class RegisterController {

    @FXML
    private TextField username_id;
    @FXML
    private TextField clientName_id;
    @FXML
    private TextField address_id;
    @FXML
    private TextField mobileNumber_id;
    @FXML
    private TextField email_id;
    @FXML
    private TextField password_id;

    @FXML
    private void initialize() {
        // You can initialize other things here if needed
    }

    @FXML
    private void onCreateAccount() {
        String username = username_id.getText();
        String clientName = clientName_id.getText();
        String address = address_id.getText();
        String mobileNumber = mobileNumber_id.getText();
        String email = email_id.getText();
        String password = password_id.getText();

        // Perform validation
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || mobileNumber.isEmpty() || address.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled out.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format.");
            return;
        }

        if (!isValidMobileNumber(mobileNumber)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid mobile number. It must be numeric and 10 digits long.");
            return;
        }

        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must be at least 6 characters long.");
            return;
        }

        // Connect to the database and insert user information
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO user (username, password, clientName, address, mobileNumber, email, userType) VALUES (?, ?, ?, ?, ?, ?, 'User')";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, clientName);
            stmt.setString(4, address);
            stmt.setString(5, mobileNumber);
            stmt.setString(6, email);
            stmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully.");

            // Clear the fields after successful registration
            username_id.clear();
            clientName_id.clear();
            address_id.clear();
            mobileNumber_id.clear();
            email_id.clear();
            password_id.clear();

            // Redirect to login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/drsystem/View/Login.fxml"));
            AnchorPane loginPage = loader.load();
            Scene loginScene = new Scene(loginPage);
            Stage currentStage = (Stage) username_id.getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login Page"); // Set title for Login page
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error occurred while creating the account.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load login page.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean isValidMobileNumber(String mobileNumber) {
        return mobileNumber.matches("\\d{10}");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    @FXML
    private void goToLoginPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/drsystem/View/Login.fxml"));
            AnchorPane loginPage = loader.load();
            Scene loginScene = new Scene(loginPage);
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login Page");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the login page.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
