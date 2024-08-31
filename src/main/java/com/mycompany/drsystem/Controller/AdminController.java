/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drsystem.Controller;

import com.mycompany.drsystem.*;
import com.mycompany.drsystem.Model.User;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author powertec
 */
public class AdminController {

    private User currentAdminUser;

    @FXML
    private void createAccount(ActionEvent event) {
        try {
            // Load the CreateAccount.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/drsystem/view/CreateAccount.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.setTitle("Create Account"); // Optional: Set the window title
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Create Account page.");
        }
    }

    @FXML
    private void deleteAccount(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/drsystem/view/DeleteAccount.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Delete Account");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Delete Account page.");
        }
    }

    public void setCurrentAdminUser(User user) {
        this.currentAdminUser = user;
    }

    @FXML
    private void redirectToAssessDisasterView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/drsystem/View/AssessDisasters.fxml"));
            Parent root = loader.load();

            // Get the controller and set the current user
            AssessDisasterController controller = loader.getController();
            controller.setCurrentUser(currentAdminUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Assess Disasters");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void redirectToEvacuationAlertView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/drsystem/view/EvacuationAlertView.fxml"));
            Parent root = loader.load();

            // Optional: Get the controller and set the current user if needed
            EvacuationAlertController controller = loader.getController();
            controller.setCurrentUser(currentAdminUser); // If you want to pass the user info to the controller

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("View Evacuation Alerts");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Evacuation Alerts page.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
