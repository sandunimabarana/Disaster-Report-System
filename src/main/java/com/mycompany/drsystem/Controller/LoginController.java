/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drsystem.Controller;

import com.mycompany.drsystem.*;
import com.mycompany.drsystem.Model.User;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;

/**
 *
 * @author powertec
 */
public class LoginController {

    @FXML
    private TextField uname_id;

    @FXML
    private PasswordField pwd_id;

    @FXML
    private Button login_btn_id;

    @FXML
    private Button register_btn_Id;

    private User currentUser;
    
    @FXML
    private void handleLogin() {
        String username = uname_id.getText();
        String password = pwd_id.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Please enter all the details.");
            return;
        }

        User user = authenticateAndGetUser(username, password);

        if (user != null) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + user.getClientName() + "!");
            redirectUser(user);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private User authenticateAndGetUser(String username, String password) {
        User user = null;
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT id, clientName, userType FROM user WHERE (username = ? OR email = ?) AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, username); // email can also be used as username
            stmt.setString(3, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String clientName = rs.getString("clientName");
                String userType = rs.getString("userType");
                user = new User(id, username, clientName, userType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private void redirectUser(User user) {
        String fxmlFile;
        if (user.getUserType().equals("User")) {
            fxmlFile = "/com/mycompany/drsystem/View/UserPage.fxml";
        } else if (user.getUserType().equals("Administrator")) {
            fxmlFile = "/com/mycompany/drsystem/View/AdminPage.fxml";
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Unknown user type.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane page = loader.load();

            if (user.getUserType().equals("Administrator")) {
            AdminController adminController = loader.getController();
            adminController.setCurrentAdminUser(user); // Pass the logged-in user to the AdminController
        } else if (user.getUserType().equals("User")) {
            UserController userController = loader.getController();
            userController.setUser(user);
        }
            Scene scene = new Scene(page);
            Stage currentStage = (Stage) login_btn_id.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle(user.getUserType() + " Page"); // Set title based on user type
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the " + user.getUserType() + " page.");
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/drsystem/View/Registration.fxml"));
            AnchorPane registrationPage = loader.load();
            Scene registrationScene = new Scene(registrationPage);
            Stage currentStage = (Stage) register_btn_Id.getScene().getWindow();
            currentStage.setScene(registrationScene);
            currentStage.setTitle("Registration Page"); // Set title for Registration page
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load registration page.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}







