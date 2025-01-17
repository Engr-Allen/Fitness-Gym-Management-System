package com.allen.fgms.Controller.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class CSController {

    @FXML
    private TextField sessionField;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox<String> startTimeComboBox;

    @FXML
    private ComboBox<String> endTimeComboBox;

    @FXML
    private TextField facilitatorField;

    @FXML
    private TextField locationField;

    @FXML
    public void initialize() {
        startTimeComboBox.getItems().addAll(
                "7:00 AM",
                "8:00 AM",
                "9:00 AM",
                "10:00 AM",
                "11:00 AM",
                "12:00 NN",
                "1:00 PM",
                "2:00 PM",
                "3:00 PM",
                "4:00 PM",
                "5:00 PM",
                "6:00 PM",
                "7:00 PM",
                "8:00 PM"
        );

        endTimeComboBox.getItems().addAll(
                "8:00 AM",
                "9:00 AM",
                "10:00 AM",
                "11:00 AM",
                "12:00 NN",
                "1:00 PM",
                "2:00 PM",
                "3:00 PM",
                "4:00 PM",
                "5:00 PM",
                "6:00 PM",
                "7:00 PM",
                "8:00 PM",
                "9:00 PM"
        );
    }

    @FXML
    void onConfirmedButtonClicked(ActionEvent event) {
        String session = sessionField.getText();
        LocalDate date = dateField.getValue();
        String startTime = startTimeComboBox.getValue();
        String endTime = endTimeComboBox.getValue();
        String facilitator = facilitatorField.getText();
        String location = locationField.getText();

        if (session.isEmpty() || date == null || startTime == null || endTime == null || facilitator.isEmpty() || location.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all fields.");
            return;
        }

        try (Connection conn = com.allen.fgms.Model.DBConnection.getConnection()) {
            String query = "INSERT INTO dbo.Sessions (Session, Date, StartTime, EndTime, Facilitators, Location) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, session);
            stmt.setDate(2, java.sql.Date.valueOf(date));
            stmt.setString(3, startTime);
            stmt.setString(4, endTime);
            stmt.setString(5, facilitator);
            stmt.setString(6, location);

            stmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Session created successfully.");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while creating the session: " + e.getMessage());
            e.printStackTrace();
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