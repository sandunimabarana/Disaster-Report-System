/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drsystem.Controller;

import com.mycompany.drsystem.DatabaseConnection;
import com.mycompany.drsystem.Model.Disaster;
import com.mycompany.drsystem.Model.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author powertec
 */
public class AssessDisasterController {

    @FXML
    private TableView<Disaster> disasterTableView;
    @FXML
    private TableColumn<Disaster, String> disasterTypeColumn;
    @FXML
    private TableColumn<Disaster, String> locationColumn;
    @FXML
    private TableColumn<Disaster, String> descriptionColumn;
    @FXML
    private TableColumn<Disaster, String> statusColumn;
    @FXML
    private TableColumn<Disaster, Void> actionsColumn;

    private User currentAdminUser;

    @FXML
    public void initialize() {
        disasterTypeColumn.setCellValueFactory(new PropertyValueFactory<>("disasterType"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        addActionsColumn(); // Add the action buttons to each row

        loadDisasterData(); // Load data into the table
    }

    public void setCurrentUser(User user) {
        this.currentAdminUser = user;
    }

    // Method to load disaster data into the table
    private void loadDisasterData() {
        List<Disaster> disasterList = new ArrayList<>();
        String query = "SELECT * FROM Disaster WHERE Status = 'New' OR Status = 'Assessed'"; // Load both new and assessed disasters

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Disaster disaster = new Disaster(
                        rs.getInt("DisasterId"),
                        rs.getString("DisasterType"),
                        rs.getString("Location"),
                        rs.getString("Description"),
                        rs.getInt("UserId"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getString("Status")
                );
                disasterList.add(disaster);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        disasterTableView.getItems().setAll(disasterList);
    }

    // Method to add action buttons to the table
    private void addActionsColumn() {
        actionsColumn.setCellFactory(new Callback<TableColumn<Disaster, Void>, TableCell<Disaster, Void>>() {
            @Override
            public TableCell<Disaster, Void> call(final TableColumn<Disaster, Void> param) {
                final TableCell<Disaster, Void> cell = new TableCell<Disaster, Void>() {
                    private final Button assessButton = new Button("Assess the Disaster");
                    private final Button alertButton = new Button("Send Evacuation Alert");

                    {
                        assessButton.setOnAction((ActionEvent event) -> {
                            Disaster disaster = getTableView().getItems().get(getIndex());
                            assessDisaster(disaster, assessButton);
                        });

                        alertButton.setOnAction((ActionEvent event) -> {
                            Disaster disaster = getTableView().getItems().get(getIndex());
                            sendEvacuationAlert(disaster, alertButton);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Disaster disaster = getTableView().getItems().get(getIndex());
                            assessButton.setDisable(disaster.getStatus().equals("Assessed"));
                            alertButton.setDisable(hasSentAlert(disaster));
                            HBox hBox = new HBox(10, assessButton, alertButton);
                            setGraphic(hBox);
                        }
                    }
                };
                return cell;
            }
        });
    }

    private void assessDisaster(Disaster selectedDisaster, Button assessButton) {
        if (selectedDisaster != null) {
            String query = "UPDATE Disaster SET Status = 'Assessed' WHERE DisasterId = ?";

            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, selectedDisaster.getDisasterId());
                stmt.executeUpdate();

                // Update the disaster object's status locally
                selectedDisaster.setStatus("Assessed");

                // Show success alert
                showAlert(Alert.AlertType.INFORMATION, "Success", "Disaster assessed successfully!");

                // Disable the assess button
                assessButton.setDisable(true);

                // Refresh the table to show the updated status
                disasterTableView.refresh();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendEvacuationAlert(Disaster selectedDisaster, Button alertButton) {
        if (selectedDisaster != null) {
            if (currentAdminUser == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Admin user is not set.");
                return;
            }

            int adminUserId = currentAdminUser.getId();
            String query = "INSERT INTO Alert (disaster_id, alert_message, sent_by) VALUES (?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, selectedDisaster.getDisasterId());
                stmt.setString(2, "Evacuation alert for " + selectedDisaster.getLocation());
                stmt.setInt(3, adminUserId);

                stmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Evacuation alert sent successfully!");

                // Disable the alert button
                alertButton.setDisable(true);

                // Refresh the table to reflect any changes
                loadDisasterData();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

// Method to check if an alert has been sent for a disaster
    private boolean hasSentAlert(Disaster disaster) {
        // Implement logic to determine if an alert has been sent for this disaster
        // For demonstration, it is assumed that if the status is 'Alert Sent', it means an alert has been sent.
        return "Alert Sent".equals(disaster.getStatus());
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
            showAlert(Alert.AlertType.INFORMATION, "Error", "Failed to load the Admin Dashboard.");
        }
    }
    
    // Method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
