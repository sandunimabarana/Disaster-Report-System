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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author powertec
 */
public class UserController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button report_disaster_btn_id;

    private User user; // Add a User field

    // Method to set the User object
    public void setUser(User user) {
        this.user = user;
        welcomeLabel.setText("Welcome, " + user.getClientName());
    }

    @FXML
    private void handleReportDisaster() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/drsystem/View/ReportDisaster.fxml"));
            AnchorPane reportDisasterPage = loader.load();

            ReportDisasterController reportDisasterController = loader.getController();
            reportDisasterController.setCurrentUser(this.user); // Pass the User object with the correct ID

            Scene reportDisasterScene = new Scene(reportDisasterPage);
            Stage currentStage = (Stage) report_disaster_btn_id.getScene().getWindow();
            currentStage.setScene(reportDisasterScene);
            currentStage.setTitle("Report Disaster");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load report disaster page.");
        }
    }

    protected void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
