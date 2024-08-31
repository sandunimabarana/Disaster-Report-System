/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drsystem.Controller;

import com.mycompany.drsystem.DatabaseConnection;
import com.mycompany.drsystem.Model.Alert;
import com.mycompany.drsystem.Model.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author powertec
 */
public class EvacuationAlertController {

    @FXML
    private TableView<Alert> alertTable;

    @FXML
    private TableColumn<Alert, Integer> alertIdColumn;

    @FXML
    private TableColumn<Alert, Integer> disasterIdColumn;

    @FXML
    private TableColumn<Alert, String> alertMessageColumn;

    @FXML
    private TableColumn<Alert, String> sentByColumn;

    @FXML
    private TableColumn<Alert, String> sentAtColumn;

    @FXML
    private TableColumn<Alert, String> statusColumn;

    @FXML
    private TableColumn<Alert, Void> removeEvacuationColumn;

    private ObservableList<Alert> alertList = FXCollections.observableArrayList();

    private User currentAdminUser;

    public void initialize() {
        alertIdColumn.setCellValueFactory(new PropertyValueFactory<>("alertId"));
        disasterIdColumn.setCellValueFactory(new PropertyValueFactory<>("disasterId"));
        alertMessageColumn.setCellValueFactory(new PropertyValueFactory<>("alertMessage"));
        sentByColumn.setCellValueFactory(new PropertyValueFactory<>("sentByUsername")); // Update this line
        sentAtColumn.setCellValueFactory(new PropertyValueFactory<>("sentAt"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load data into the table
        loadAlertData();

        // Add the remove evacuation button to each row
        addButtonToTable();
    }

    public void setCurrentUser(User user) {
        this.currentAdminUser = user;
    }

    private void loadAlertData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            String query = "SELECT Alert.alert_id, Alert.disaster_id, Alert.alert_message, User.username AS sentByUsername, Alert.sent_at, Alert.status "
                    + "FROM Alert "
                    + "JOIN User ON Alert.sent_by = User.id";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                alertList.add(new Alert(
                        rs.getInt("alert_id"),
                        rs.getInt("disaster_id"),
                        rs.getString("alert_message"),
                        rs.getString("sentByUsername"), // Update this line
                        rs.getTimestamp("sent_at"),
                        rs.getString("status")
                ));
            }
            alertTable.setItems(alertList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable() {
        Callback<TableColumn<Alert, Void>, TableCell<Alert, Void>> cellFactory = new Callback<TableColumn<Alert, Void>, TableCell<Alert, Void>>() {
            @Override
            public TableCell<Alert, Void> call(final TableColumn<Alert, Void> param) {
                final TableCell<Alert, Void> cell = new TableCell<Alert, Void>() {

                    private final Button btn = new Button("Remove Evacuation");

                    {
                        btn.setOnAction(event -> {
                            Alert alert = getTableView().getItems().get(getIndex());
                            closeEvacuation(alert, btn); // Pass the button reference
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Check if the alert status is 'Closed' and disable the button if so
                            Alert alert = getTableView().getItems().get(getIndex());
                            btn.setDisable("Closed".equals(alert.getStatus()));
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        removeEvacuationColumn.setCellFactory(cellFactory);
    }

    private void closeEvacuation(Alert alert, Button btn) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            // Update the Alert table
            String updateAlertQuery = "UPDATE Alert SET Status = 'Closed', ClosedDate = NOW() WHERE alert_id = ?";
            PreparedStatement pstmtAlert = conn.prepareStatement(updateAlertQuery);
            pstmtAlert.setInt(1, alert.getAlertId());
            pstmtAlert.executeUpdate();

            // Update the Disaster table
            String updateDisasterQuery = "UPDATE Disaster SET Status = 'Closed' WHERE DisasterId = ?";
            PreparedStatement pstmtDisaster = conn.prepareStatement(updateDisasterQuery);
            pstmtDisaster.setInt(1, alert.getDisasterId());
            pstmtDisaster.executeUpdate();

            // Update the alert object and refresh the table view
            alert.setStatus("Closed");
            alertTable.refresh();

            // Disable the button after closing the evacuation
            btn.setDisable(true);

        } catch (Exception e) {
            e.printStackTrace();
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
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Admin Dashboard.");
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
