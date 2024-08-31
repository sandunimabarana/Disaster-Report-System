/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drsystem.Controller;

import com.mycompany.drsystem.DatabaseConnection;
import com.mycompany.drsystem.Model.Department;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author powertec
 */
public class CreateAccountController {

    @FXML
    private ComboBox<Department> departmentComboBox;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailTextField;

    @FXML
    private void initialize() {
        loadDepartments();
    }

    private void loadDepartments() {
        ObservableList<Department> departments = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT department_id, department_name FROM Department")) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int departmentId = resultSet.getInt("department_id");
                String departmentName = resultSet.getString("department_name");
                departments.add(new Department(departmentId, departmentName));
            }
            departmentComboBox.setItems(departments);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load departments: " + e.getMessage());
        }
    }

    @FXML
    private void saveAccount() {
        Department selectedDepartment = departmentComboBox.getValue();
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String email = emailTextField.getText();

        // Validation
        if (selectedDepartment == null || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert("Validation Error", "Please enter all fields.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO User (username, password, clientName, address, mobileNumber, email, userType, department_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, "");  // clientName is left blank because it's optional
            statement.setString(4, "");  // address is left blank because it's optional
            statement.setString(5, "");  // mobileNumber is left blank because it's optional
            statement.setString(6, email);
            statement.setString(7, "DepartmentUser");  // Assuming you want to save as 'DepartmentUser'
            statement.setInt(8, selectedDepartment.getDepartmentId());

            statement.executeUpdate();
            showAlert("Success", "Account created successfully.");

            // Clear input fields after successful account creation
            clearInputFields();
        } catch (SQLException e) {
            showAlert("Error", "Failed to create account: " + e.getMessage());
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

    private void clearInputFields() {
        usernameTextField.clear();
        passwordField.clear();
        emailTextField.clear();
        departmentComboBox.setValue(null);  // Reset ComboBox selection
    }
}
